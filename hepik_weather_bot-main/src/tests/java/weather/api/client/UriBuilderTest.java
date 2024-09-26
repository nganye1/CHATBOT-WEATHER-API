package tests.java.weather.api.client;

import main.java.weather.api.QueryParameter;
import main.java.weather.api.client.ApiService;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriBuilderTest {
    private final String URL = "https://hello.from";

    private final ApiService.UriBuilder URI_BUILDER = new ApiService.UriBuilder(URL);

    @Test
    public void testGeneratesUriFromUrl() {
        String expectedURL = URL + "?place=world";

        URI expectedURI = URI.create(expectedURL);

        assertEquals(expectedURI, URI_BUILDER.generateURI(new QueryParameter("place", "world")));
    }
}