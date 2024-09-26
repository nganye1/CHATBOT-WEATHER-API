package main.java.weather;

import main.java.weather.api.client.ApiResponse;
import main.java.weather.api.forecast.OpenMeteoForecastService;
import main.java.weather.api.geocoding.GeocodingService;
import main.java.weather.api.forecast.ForecastService;
import main.java.weather.api.geocoding.OpenMeteoGeocodingService;
import main.java.weather.data.Forecast;
import main.java.weather.data.GeoLocation;
import main.java.weather.data.TripDestination;
import main.java.weather.data.WeatherForecast;
import main.java.weather.data.json_bindings.ForecastResult;
import main.java.weather.data.json_bindings.LocationResult;

import java.util.*;

/**
 * Represents a high-level class for the chatbot for fetching {@link GeoLocation} information of {@link TripDestination}
 * and {@link WeatherForecast} information of {@link GeoLocation}.
 *
 * @author Kelvin Kavisi
 */
public class ApiManager {
    private final GeocodingService GEOCODING_SERVICE;

    private final ForecastService FORECAST_SERVICE;

    public ApiManager(GeocodingService geocodingService, ForecastService forecastService) {
        this.GEOCODING_SERVICE = geocodingService;
        this.FORECAST_SERVICE = forecastService;
    }

    public static ApiManager forOpenMeteo() {
        return new ApiManager(new OpenMeteoGeocodingService(), new OpenMeteoForecastService());
    }

    /**
     * Fetches the geographical information of {@link TripDestination}s.
     *
     * @param destinations denotes an array of destinations to fetch
     * @return {@link ApiResponse} with a list of {@link GeoLocation} for each destination if successful.
     */
    public ApiResponse<List<GeoLocation>, TripDestination> fetchGeolocations(TripDestination... destinations) {
        // Fetch response
        ApiResponse<LocationResult[], Integer> fetchedLocations = GEOCODING_SERVICE.fetchLocations(destinations);

        // Create utility instance
        ApiManagerUtil<TripDestination, LocationResult, GeoLocation> util = new ApiManagerUtil<>();

        LinkedHashMap<TripDestination, LocationResult> hashMap = util
                .extractAndMap(destinations, fetchedLocations, "No such city was found");

        HashMap<TripDestination, String> errors = util.ERRORS;

        // Further filter results and retain only keys that have a valid match
        Set<TripDestination> destinationsWithResult = Set.copyOf(hashMap.keySet());

        ArrayList<GeoLocation> geoLocations = new ArrayList<>();

        for (TripDestination destination : destinationsWithResult) {
            LocationResult locationResult = hashMap.remove(destination);

            // Find first match
            Optional<LocationResult.FetchedLocation> nullableLocation = Arrays
                    .stream(locationResult.locations())
                    .filter(fetchedLocation -> fetchedLocation.equalsDestination(destination))
                    .findFirst();

            if (nullableLocation.isPresent())
                geoLocations.add(GeoLocation.of(destination, nullableLocation.get()));
            else
                errors.put(destination, "No such city was found");
        }

        return util.mapToResponse(geoLocations, errors);
    }

    /**
     * Fetches the weather forecasts of {@link GeoLocation}s.
     *
     * @param locations denotes an array of locations whose forecast needs to be fetched.
     * @return {@link ApiResponse} with a list of {@link WeatherForecast} if successful.
     */
    public ApiResponse<List<WeatherForecast>, GeoLocation> fetchForecasts(GeoLocation... locations) {
        // Fetch response
        ApiResponse<ForecastResult[], Integer> fetchedForecasts = FORECAST_SERVICE.fetchDailyForecasts(locations);

        // Create utility instance
        ApiManagerUtil<GeoLocation, ForecastResult, WeatherForecast> util = new ApiManagerUtil<>();

        util.extractAndMap(locations, fetchedForecasts, "No forecast information found");

        ArrayList<WeatherForecast> weatherForecasts = new ArrayList<>();

        // Geolocations don't need further inspection. They are safe-ish
        for (Map.Entry<GeoLocation, ForecastResult> entry : util.FILTERED_SUCCESS.sequencedEntrySet()) {
            GeoLocation location = entry.getKey();
            ForecastResult.DailyForecast dailyForecast = entry.getValue().forecast();

            String[] availableDates = dailyForecast.dates();
            Forecast[] forecasts = Forecast.fromObject(dailyForecast);

            weatherForecasts.add(new WeatherForecast(location, availableDates, forecasts));
        }

        return util.mapToResponse(weatherForecasts, util.ERRORS);
    }

    /**
     * Represents a private utility class with utility methods for extracting from a {@link ApiResponse} and
     * outputting out to one.
     * <p></p>
     * Members are not encapsulated as they are for {@link ApiManager}'s convenience.
     *
     * @param <Q> denotes the original Java type used for making api request
     * @param <R> denotes the Java type mapped from the response.
     * @param <S> denotes the Java type returned by the {@link ApiManager}
     */
    private static class ApiManagerUtil<Q, R, S> {
        public final LinkedHashMap<Q, R> FILTERED_SUCCESS = new LinkedHashMap<>();
        public final HashMap<Q, String> ERRORS = new HashMap<>();

        /**
         * Extracts and maps each original request to an
         * object mapped by the {@link main.java.weather.api.JsonExtractor}.
         *
         * @param origin          denotes an array of filters for the requests
         * @param serviceResponse denotes an {@link ApiResponse} returned from the api request
         * @return the current LinkedHashMap storing successful results
         */
        public LinkedHashMap<Q, R> extractAndMap(Q[] origin, ApiResponse<R[], Integer> serviceResponse, String nullErrorMessage) {
            R[] success = serviceResponse.getSuccess();
            Set<Integer> errorIndices = serviceResponse.getIndicesWithErrors(); // Get all error indices

            // Keep track of indices in the success array
            int nextSuccessIndex = 0;

            for (int i = 0; i < origin.length; i++) {
                Q current = origin[i];

                // Add error if found instead of the object
                if (errorIndices.contains(i)) {
                    ERRORS.put(current, serviceResponse.getError(i));
                } else {
                    R value = success[nextSuccessIndex++]; // The next accessor will have the updated value

                    if (value == null)
                        ERRORS.put(current, nullErrorMessage);
                    else
                        FILTERED_SUCCESS.put(current, value);
                }
            }

            return FILTERED_SUCCESS;
        }

        /**
         * Creates an {@link ApiResponse} object.
         *
         * @param success denotes the success object to add.
         * @param errors  denotes the errors to be added to the response
         * @return an {@link ApiResponse} object.
         */
        public ApiResponse<List<S>, Q> mapToResponse(ArrayList<S> success, HashMap<Q, String> errors) {
            ApiResponse<List<S>, Q> response = new ApiResponse<>(); // Response

            // Loop all errors and add them
            for (Map.Entry<Q, String> entry : errors.entrySet()) {
                response.addError(entry.getKey(), entry.getValue());
            }

            // Add empty list
            response.setSuccess(Collections.unmodifiableList(success));
            return response.seal();
        }
    }
}
