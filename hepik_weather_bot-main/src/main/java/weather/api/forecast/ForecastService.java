package main.java.weather.api.forecast;

import main.java.weather.api.client.ApiResponse;
import main.java.weather.data.GeoLocation;
import main.java.weather.data.json_bindings.ForecastResult;

/**
 * Represents a class that fetches the weather forecast of {@link GeoLocation}
 *
 * @author Kelvin Kavisi
 */
public interface ForecastService {
    /**
     * Fetches the weather forecast of a {@link GeoLocation}
     *
     * @param locations denotes an array of locations whose forecast needs to be fetched.
     * @return {@link ApiResponse} with an array of {@link ForecastResult} if successful.
     */
    ApiResponse<ForecastResult[], Integer> fetchDailyForecasts(GeoLocation... locations);
}
