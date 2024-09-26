package main.java.phpBridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import main.java.weather.ApiManager;
import main.java.weather.api.JsonExtractor;
import main.java.weather.api.client.ApiResponse;
import main.java.weather.data.GeoLocation;
import main.java.weather.data.TripDestination;
import main.java.weather.data.WeatherForecast;
import main.java.weather.interpreter.WeatherInterpreter;

import java.util.*;

import static main.java.phpBridge.PhpBridgeUtil.*;

/**
 * Represents a generic runnable Java class that acts as the bridge between PHP & Java.
 * <p>
 *
 * This class is executed via the shell in PHP.
 *
 * @author Kelvin Kavisi
 */
public class PhpBridge {
    private static final ApiManager API_MANAGER = ApiManager.forOpenMeteo();

    public static void main(String[] args) {
        try {
            int numOfArgs = args.length;

            // Must receive arguments
            if (numOfArgs != 1) {
                String terminal = numOfArgs > 1 ? "more" : "none";

                System.out.println(handleUnknownRequest("Expected only one argument but found " + terminal));
                return;
            }

            // Get our custom request
            PhpRequest request = extractPhpRequest(args[0]);

            String output = switch (request.target()) {
                case REQUEST_LOCATION -> handleLocationRequest(request.input());
                case REQUEST_WEATHER -> handleWeatherRequest(request.input());
                default -> handleUnknownRequest("Invalid arguments passed to bridge.");
            };

            System.out.println(output);
        } catch (Exception exception) {
            System.out.println("E: " + exception.getMessage());
        }
    }

    private static String handleUnknownRequest(String message) throws JsonProcessingException {
        String errorMessage = "Internal Server error. " + message;

        return buildErrorOnly("input", errorMessage);
    }

    private static String handleLocationRequest(String input) throws JsonProcessingException {
        /*
         * PHP stores  1-to-1 mapping of the TripDestination map while requesting from user. It passes in an encoded
         * json list with the maps that map to a TripDestination object.
         *
         * [{"city": .., "country": .., "startDate": .., "endDate": ..},..]
         */
        JsonUtil<TripDestination> util = new JsonUtil<>();

        TripDestination[] destinations = util.extractFromInput(input, TripDestination.class).toArray(TripDestination[]::new);

        ApiResponse<List<GeoLocation>, TripDestination> response = API_MANAGER.fetchGeolocations(destinations);

        String success = util.JSON_EXTRACTOR.convertToJson(response.getSuccess());

        List<Map<String, Object>> listOfErrors = response.getErrors().entrySet().stream().map(entry -> {
            TripDestination destination = entry.getKey();
            entry.setValue(util.generalizeError(entry.getValue()) + " for " + destination.getLocality());

            return Map.of("location", destination, "error", entry.getValue());
        }).toList();

        return buildOutput(success, "api", listOfErrors);
    }

    private static String handleWeatherRequest(String input) throws JsonProcessingException {
        /*
         * If the GeoLocation fetch was successful and any inconsistencies corrected, PHP passes in an encoded json
         * list with the maps that map to a GeoLocation object.
         */
        JsonUtil<GeoLocation> util = new JsonUtil<>();

        GeoLocation[] locations = util.extractFromInput(input, GeoLocation.class).toArray(GeoLocation[]::new);

        ApiResponse<List<WeatherForecast>, GeoLocation> response = API_MANAGER.fetchForecasts(locations);

        // Interpret all weather forecasts & map each to a destination
        List<String> interpreted = response.getSuccess()
                .stream()
                .map(forecast -> {
                    // Get locality i.e. city,country.
                    String locality = forecast.getLocation().destination().getLocality();

                    // Interpret weather
                    return formatInterpretation(locality, WeatherInterpreter.interpretWeather(forecast));
                }).toList();

        String success = util.JSON_EXTRACTOR.convertToJson(interpreted);

        List<String> errors = response.getErrors()
                .entrySet()
                .stream()
                .map(entry -> util.generalizeError(entry.getValue()) + " for " + entry.getKey().destination().getLocality())
                .toList();

        return buildOutput(success, "api", errors);
    }


    private static class JsonUtil<T> {
        public final JsonExtractor<T> JSON_EXTRACTOR = new JsonExtractor<>();

        public List<T> extractFromInput(String input, Class<T> tClass) throws JsonProcessingException {
            ArrayList<T> arrayList = new ArrayList<>();

            // Read parsed json
            ArrayNode parsedTree = (ArrayNode) JSON_EXTRACTOR.readJsonTree(input);

            Iterator<JsonNode> iterator = parsedTree.elements();

            while (iterator.hasNext()) {
                // Parse to object and add to list
                arrayList.add(JSON_EXTRACTOR.extractFromJson(iterator.next(), tClass));
            }

            return arrayList;
        }

        public String generalizeError(String error) {
            return switch (error) {
                case "No such city was found", "No forecast information found" -> error;
                default -> "I encountered an error while fetching information";
            };
        }
    }
}
