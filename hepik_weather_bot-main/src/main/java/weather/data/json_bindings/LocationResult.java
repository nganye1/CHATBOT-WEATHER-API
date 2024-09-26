package main.java.weather.data.json_bindings;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.weather.data.TripDestination;

/**
 * Represents a json binding class to hold data extracted from the <a href="https://open-meteo.com/">Open Meteo</a>
 * geocoding response.
 *
 * @param locations denotes an array of {@link FetchedLocation} extracted from the parsed json response.
 */
public record LocationResult(@JsonProperty("results")FetchedLocation[] locations) {

    /**
     * Represents a data for mapping a json response from a request sent to a geocoding service.
     *
     * @param name represents the name of searched location
     * @param country represents the country of where this location is located.
     * @param latitude denotes its bearing from the equator.
     * @param longitude denotes its bearing from the prime meridian.
     * @param elevation denotes its average height above sea level.
     *
     * @author Kelvin Kavisi
     */
    public record FetchedLocation(String name, String country, double latitude, double longitude, double elevation) {

        /**
         * Checks if a {@link TripDestination} is a match
         * @param destination denotes a destination object to check
         * @return true if it matches
         */
        public boolean equalsDestination(TripDestination destination) {
            return destination.city().equalsIgnoreCase(name) && destination.country().equalsIgnoreCase(country);
        }
    }
}


