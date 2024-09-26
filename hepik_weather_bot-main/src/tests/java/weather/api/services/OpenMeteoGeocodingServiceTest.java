package tests.java.weather.api.services;

import main.java.weather.api.geocoding.OpenMeteoGeocodingService;
import main.java.weather.data.TripDestination;
import org.junit.jupiter.api.Test;
import tests.java.weather.TestHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenMeteoGeocodingServiceTest {
    private final OpenMeteoGeocodingService GEOCODING_SERVICE = new OpenMeteoGeocodingService();

    private final TripDestination TRIP_DESTINATION = TestHelper.getTestDestination();

    @Test
    public void testFetchLocations() {
        assertNotNull(GEOCODING_SERVICE.fetchLocations(TRIP_DESTINATION));
    }
}
