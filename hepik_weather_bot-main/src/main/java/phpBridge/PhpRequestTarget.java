package main.java.phpBridge;

/**
 * Represents a request made by PHP. Typically, PHP will collect all the required user input and rely on Java for
 * API calls.
 * <p>
 * However, this can entirely be achieved in PHP.
 *
 * @author Kelvin Kavisi
 */
public enum PhpRequestTarget {
    /**
     * Invalid request. Failsafe for unknown requests
     */
    UNKNOWN,

    /**
     * PHP request for fetching {@link main.java.weather.data.GeoLocation} info
     */
    REQUEST_LOCATION,

    /**
     * PHP request for fetching & interpreting {@link main.java.weather.data.WeatherForecast}
     */
    REQUEST_WEATHER;

    public static PhpRequestTarget fromString(String string) {
        return switch (string.replaceAll("\"", "")) {
            case "location" -> PhpRequestTarget.REQUEST_LOCATION;
            case "weather" -> PhpRequestTarget.REQUEST_WEATHER;
            default -> PhpRequestTarget.UNKNOWN;
        };
    }
}
