package main.java.weather.api.geocoding;

import main.java.weather.api.QueryParameter;
import main.java.weather.api.client.ApiService;
import main.java.weather.api.client.ApiResponse;
import main.java.weather.data.TripDestination;
import main.java.weather.data.json_bindings.LocationResult;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a class that fetches geographical information from <a href="https://open-meteo.com/">Open Meteo website</a>
 *
 * @author Kelvin Kavisi
 */
public class OpenMeteoGeocodingService extends ApiService<LocationResult> implements GeocodingService {
    private static final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1";

    public OpenMeteoGeocodingService() {
        super(GEOCODING_URL, "search");
    }

    @Override
    public ApiResponse<LocationResult[], Integer> fetchLocations(TripDestination... destinations) {
        // Must pass in some destinations
        if (destinations.length == 0) return null;

        // OpenMeteo only allow a single city search in one request. Create params for each
        QueryParameter[] parameters = Arrays
                .stream(destinations)
                .map(destination -> new QueryParameter("name", destination.city()))
                .toArray(QueryParameter[]::new);

        // Create a URI for each
        UriBuilder builder = super.getUriBuilder();

        ArrayList<URI> uris = new ArrayList<>();

        for (QueryParameter queryParameter: parameters)
            uris.add(builder.generateURI(queryParameter));

        // Make multiple asynchronous requests
        HttpResponse<String>[] responses = super.sendRequest(uris.toArray(URI[]::new));

        return super.extractHttpResponses(responses, "results", LocationResult.class);
    }

}
