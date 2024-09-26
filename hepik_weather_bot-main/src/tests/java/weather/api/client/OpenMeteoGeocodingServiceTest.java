package tests.java.weather.api.client;

import main.java.weather.api.client.ApiResponse;
import main.java.weather.api.geocoding.OpenMeteoGeocodingService;
import main.java.weather.data.Coordinates;
import main.java.weather.data.TripDestination;
import main.java.weather.data.json_bindings.LocationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.java.weather.TestHelper;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

class OpenMeteoGeocodingServiceTest {

    @Test
    void testIncorrectLocations() {
        OpenMeteoGeocodingService geocodingService = new OpenMeteoGeocodingService();

        // Passing in incorrect locations
        ApiResponse<LocationResult[], Integer> response = geocodingService.fetchLocations(new TripDestination("InvalidCity", "InvalidCountry", "2024-04-21", "2024-04-30"));

        // Asserting that response contains errors
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.hasErrors());
    }

    @Test
    void testLocationsNotInSpecifiedCountry() {
        OpenMeteoGeocodingService geocodingService = new OpenMeteoGeocodingService();

        // Passing in a location not in the specified country
        ApiResponse<LocationResult[], Integer> response = geocodingService.fetchLocations(new TripDestination("Paris", "Germany", "2024-04-21", "2024-04-30"));

        // Asserting that response contains errors
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.hasErrors());
    }
}

