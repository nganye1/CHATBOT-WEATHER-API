package tests.java.weather.api;

import main.java.weather.ApiManager;
import org.junit.jupiter.api.Test;
import tests.java.weather.TestHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiManagerTest {
    private final ApiManager API_MANAGER = ApiManager.forOpenMeteo();

    @Test
    public void testFetchLocations() {
        assertNotNull(API_MANAGER.fetchGeolocations(TestHelper.getTestDestination()));
    }

    @Test
    public void testFetchForecasts() {
        assertNotNull(API_MANAGER.fetchForecasts(TestHelper.getTestLocation()));
    }
}
