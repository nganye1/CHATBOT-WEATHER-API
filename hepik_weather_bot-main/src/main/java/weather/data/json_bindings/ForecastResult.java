package main.java.weather.data.json_bindings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import main.java.weather.data.Coordinates;
import main.java.weather.data.GeoLocation;

/**
 * Represents a json binding class to hold data extracted from the <a href="https://open-meteo.com/">Open Meteo</a>
 * forecast response.
 *
 * @param latitude denotes its bearing from the equator.
 * @param longitude denotes its bearing from the prime meridian.
 * @param elevation denotes its average height above sea level.
 *                    fetched.
 * @param forecast denotes the forecast of a {@link GeoLocation}.
 *
 * @author Kelvin Kavisi
 */
public record ForecastResult(double latitude, double longitude, double elevation, @JsonProperty("daily") DailyForecast forecast) {

    /**
     * Represents a json binding class for mapping the forecast result sent by a weather forecast service.
     *
     * @param dates represents an array of iso8601 formatted dates
     * @param weatherCodes represents an array of standard WMO codes
     * @param maxTemperatures represents an array of temperatures
     * @param minTemperatures represents an array of temperatures
     * @param maxApparentTemperatures represents an array of temperatures
     * @param minApparentTemperatures represents an array of temperatures
     * @param daylightDurations represents an array of seconds denoting daylight durations
     * @param sunshineDurations represents an array of seconds denoting sunshine durations
     * @param precipitationProbabilities represents an array of decimals denoting probability of precipitation
     * @param windSpeeds represents an array of decimals denoting the max wind speed in km/hr
     *
     * @author Kelvin Kavisi
     */
    @JsonRootName("daily")
    public record DailyForecast(
        @JsonProperty("time")
        String[] dates,

        @JsonProperty("weather_code")
        int[] weatherCodes,

        @JsonProperty("temperature_2m_max")
        double[] maxTemperatures,

        @JsonProperty("temperature_2m_min")
        double[] minTemperatures,

        @JsonProperty("apparent_temperature_max")
        double[] maxApparentTemperatures,

        @JsonProperty("apparent_temperature_min")
        double[] minApparentTemperatures,

        @JsonProperty("daylight_duration")
        double[] daylightDurations,

        @JsonProperty("sunshine_duration")
        double[] sunshineDurations,

        @JsonProperty("precipitation_probability_max")
        double[] precipitationProbabilities,

        @JsonProperty("wind_speed_10m_max")
        double[] windSpeeds
    ) {}
}
