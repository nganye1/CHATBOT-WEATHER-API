package tests.java.weather.api.services;
import main.java.weather.api.forecast.OpenMeteoForecastService;
import main.java.weather.data.Coordinates;
import main.java.weather.data.GeoLocation;
import main.java.weather.data.TripDestination;
import main.java.weather.api.client.ApiResponse;
import main.java.weather.data.json_bindings.ForecastResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OpenMeteoForecastServiceTest {
    private OpenMeteoForecastService service;

    @BeforeEach
    public void setUp() {
        service = new OpenMeteoForecastService();
    }

    @Test
    public void testFetchDailyForecasts_InvalidDates() {
        GeoLocation location = new GeoLocation(
                new TripDestination("TestCity", "TestCountry", "02-2024-30", "2024-02-31"), // Invalid dates
                new Coordinates(48.8566, 2.3522, 35)
        );

        ApiResponse<ForecastResult[], Integer> response = service.fetchDailyForecasts(location);
        assertNotNull(response);
        assertTrue(response.hasErrors());
    }

    @Test
    public void testInvalidCoordinates() {
        assertThrows(NullPointerException.class, () -> {
            GeoLocation location = new GeoLocation(
                    new TripDestination("TestCity", "TestCountry", "2024-04-10", "2024-04-20"),
                    null
            );
             service.fetchDailyForecasts(location);
        });
    }
}
