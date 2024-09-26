package main.java.weather.data;

import main.java.weather.data.json_bindings.LocationResult;


/**
 * Represents a data object to store the geographical coordinates of a {@link TripDestination}
 *
 * @param latitude denotes its bearing from the equator.
 * @param longitude denotes its bearing from the prime meridian.
 * @param elevation denotes its average height above sea level.
 *
 * @author Kelvin Kavisi
 */
public record Coordinates(double latitude, double longitude, double elevation) {
    /**
     * Creates a {@link Coordinates} object from a single {@link LocationResult.FetchedLocation} object.
     *
     * @param fetchedLocation denotes the {@link LocationResult.FetchedLocation} object.
     * @return a {@link LocationResult.FetchedLocation} object
     */
    public static Coordinates fromObject(LocationResult.FetchedLocation fetchedLocation) {
        return new Coordinates(fetchedLocation.latitude(), fetchedLocation.longitude(), fetchedLocation.elevation());
    }
}
