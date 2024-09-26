package main.java.weather.api.forecast;

import main.java.weather.api.QueryParameter;
import main.java.weather.api.client.ApiService;
import main.java.weather.api.client.ApiResponse;
import main.java.weather.data.Coordinates;
import main.java.weather.data.GeoLocation;
import main.java.weather.data.TripDestination;
import main.java.weather.data.json_bindings.ForecastResult;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Represents a class that fetches the weather forecast from <a href="https://open-meteo.com/">Open Meteo website</a>
 *
 * @author Kelvin Kavisi
 */
public class OpenMeteoForecastService extends ApiService<ForecastResult> implements ForecastService {
    private static final String WEATHER_FORECAST_URL = "https://api.open-meteo.com/v1";

    private final String[] DEFAULT_DAILY_VALUES = {
            "weather_code",
            "temperature_2m_max",
            "temperature_2m_min",
            "apparent_temperature_max",
            "apparent_temperature_min",
            "daylight_duration",
            "sunshine_duration",
            "precipitation_probability_max",
            "wind_speed_10m_max",
    };

    public OpenMeteoForecastService() {
        super(WEATHER_FORECAST_URL, "forecast");
    }

    @Override
    public ApiResponse<ForecastResult[], Integer> fetchDailyForecasts(GeoLocation... locations) {
        // Must pass in some locations
        if (locations.length == 0) return null;

        // Default daily parameter
        QueryParameter defaultParam = new QueryParameter("daily", String.join(",", DEFAULT_DAILY_VALUES));

        // Loop every location and create a URI
        URI[] uris = getUris(locations, defaultParam);

        // Make multiple asynchronous requests
        HttpResponse<String>[] responses = super.sendRequest(uris);

        return super.extractHttpResponses(responses, "daily", ForecastResult.class);
    }

    private URI[] getUris(GeoLocation[] locations, QueryParameter defaultParam) {
        UriBuilder builder = super.getUriBuilder();
        ArrayList<URI> uris = new ArrayList<>();

        for (GeoLocation location: locations) {
            TripDestination destination = location.destination();
            Coordinates coordinates = location.coordinates();

            QueryParameter[] queryParams = {
                    new QueryParameter("latitude", String.valueOf(coordinates.latitude())),
                    new QueryParameter("longitude", String.valueOf(coordinates.longitude())),
                    new QueryParameter("elevation", String.valueOf(coordinates.elevation())),
                    new QueryParameter("start_date", destination.startDate()),
                    new QueryParameter("end_date", destination.endDate()),
                    defaultParam
            };

            uris.add(builder.generateURI(queryParams));
        }

        return uris.toArray(URI[]::new);
    }
}
