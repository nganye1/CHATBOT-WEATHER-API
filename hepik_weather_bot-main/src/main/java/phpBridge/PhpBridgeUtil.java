package main.java.phpBridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Represents a utility class for  {@link PhpBridge}
 *
 * @author Kelvin Kavisi
 */
public class PhpBridgeUtil {
    /**
     * Extracts input from the shell into a {@link PhpRequest} object
     *
     * @param input denotes the rest argument from the shell execution of {@link PhpBridge}
     * @return {@link PhpRequest} DTO object
     */
    public static PhpRequest extractPhpRequest(String input) throws JsonProcessingException {
        // Read json input
        JsonNode jsonNode = new ObjectMapper().readTree(input);

        String phpTarget = jsonNode.has("request") ? String.valueOf(jsonNode.get("request")) : "";
        String bridgeInput = jsonNode.has("input") ? jsonNode.get("input").toPrettyString() : "";

        return new PhpRequest(PhpRequestTarget.fromString(phpTarget), bridgeInput);
    }

    /**
     * Formats and generates a weather interpretation for a {@link main.java.weather.data.GeoLocation}
     *
     * @param locality denotes the destination in the format "city, country" i.e. Cork, Ireland
     * @param interpretationByDate denotes a map with weather interpretation for each date.
     * @return a full interpretation for the city.
     */
    public static String formatInterpretation(String locality, Map<String, String> interpretationByDate) {
        StringBuilder interpretation = new StringBuilder();

        // Add locality
        interpretation.append("For <b>").append(locality).append("</b>, <br>");

        // Merge into single interpretation
        interpretationByDate.forEach((key, value) -> {
            String date = "On the <b>" + key + "</b> - ";

            interpretation.append(date).append(value).append(".<br><br>");
        });

        return interpretation.append("<br>").toString();
    }

    /**
     * Generates a json string for an error object only. Internally calls {@link #buildOutput} within this class with a
     * success key set to "null".
     *
     * @param errorType denotes the type of error. Can be of type "api" or "input".
     * @param otherValue denotes the value to link to the error key provided.
     * @return an error object for PHP to process as a json string.
     * @throws JsonProcessingException if an error is encountered while encoding the error object to a json string
     */
    public static String buildErrorOnly(String errorType, String otherValue) throws JsonProcessingException {
        return buildOutput("null", errorType, otherValue);
    }

    /**
     * Generates a json string to be passed back to PHP.
     *
     * @param success denotes an object/string to link to the success key in the json string generated
     * @param errorType denotes the type of error. Can be of type "api" or "input".
     * @param otherErrorValue denotes the value to link to the error key provided.
     * @return an output for PHP to process as a json string.
     * @throws JsonProcessingException if an error is encountered while encoding the error object to a json string
     */
    public static String buildOutput(String success, String errorType, Object otherErrorValue) throws JsonProcessingException {
        /*
         * "error" key in response is a map
         */
        Map<String, Object> error = Map.of("type", errorType, "output", otherErrorValue);
        Map<String, Object> output = Map.of("success", success, "error", error);
        return new ObjectMapper().writeValueAsString(output);
    }
}
