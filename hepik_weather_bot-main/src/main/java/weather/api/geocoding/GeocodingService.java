package main.java.weather.api.geocoding;

import main.java.weather.api.client.ApiResponse;
import main.java.weather.data.TripDestination;
import main.java.weather.data.json_bindings.LocationResult;

/**
 * Represents a class that fetches geographical information of {@link TripDestination}
 *
 * @author Kelvin Kavisi
 */
public interface GeocodingService {
    /**
     * Fetches the geographical information of {@link TripDestination}.
     *
     * @param destinations denotes an array of destinations to fetch
     * @return {@link ApiResponse} with an array of {@link LocationResult} for each destination if successful
     */
    ApiResponse<LocationResult[], Integer> fetchLocations(TripDestination... destinations);
}
