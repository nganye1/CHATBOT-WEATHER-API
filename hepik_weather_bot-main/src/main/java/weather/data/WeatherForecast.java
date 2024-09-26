package main.java.weather.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Represents an immutable class for storing weather {@link Forecast} information for {@link GeoLocation}
 *
 * @author Kelvin Kavisi
 */
public class WeatherForecast {
    private final GeoLocation location;

    private final String[] availableDates;

    private final LinkedHashMap<String, Forecast> forecasts;

    public WeatherForecast(GeoLocation location, String[] availableDates, Forecast[] forecasts) {
        this.location = location;
        this.availableDates = Arrays.copyOf(availableDates, availableDates.length); // Make copy for immutability
        this.forecasts = mapDateToForecast(this.availableDates, forecasts);
    }

    /**
     * Obtains the {@link GeoLocation} object whose weather data is stored in this class.
     *
     * @return {@link GeoLocation} object
     */
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * Obtains the dates whose {@link Forecast} has been fetched
     *
     * @return a copy of the array storing the dates.
     */
    public String[] getAvailableDates() {
        return Arrays.copyOf(availableDates, availableDates.length); // Preserve immutability of this class
    }

    /**
     * Obtains the {@link Forecast} of a specific date.
     *
     * @param date denotes the forecast date
     *
     * @return {@link Forecast} object if present. Otherwise, null.
     */
    public Forecast getForecast(String date) {
        return forecasts.get(date);
    }

    private LinkedHashMap<String, Forecast> mapDateToForecast(String[] availableDates, Forecast[] forecasts) {
        int count = availableDates.length;

        LinkedHashMap<String, Forecast> hashMap = new LinkedHashMap<>(count);

        for (int i = 0; i < count; i++)
            hashMap.put(availableDates[i], forecasts[i]);

        return hashMap;
    }

    @Override
    public String toString() {
        return String.format("""
                {
                \tDestination: %s
                \tCoordinates: %s
                \tForecast: %s
                }""", location.destination(), location.coordinates(), forecasts
        );
    }
}
