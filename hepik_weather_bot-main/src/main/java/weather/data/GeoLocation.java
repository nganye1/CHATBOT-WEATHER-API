package main.java.weather.data;

import main.java.weather.data.json_bindings.LocationResult;

import java.util.Objects;

/**
 * Represents a data object for linking fetched {@link Coordinates} to a {@link TripDestination}.
 *
 * @param destination represents a user's {@link TripDestination} provided to the chatbot.
 * @param coordinates represents {@link Coordinates} fetched from a geocoding service for a {@link TripDestination}.
 *
 * @author Kelvin Kavisi
 */
public record GeoLocation(TripDestination destination, Coordinates coordinates) {
    public GeoLocation(TripDestination destination, Coordinates coordinates) {
        this.destination = Objects.requireNonNull(destination);
        this.coordinates = Objects.requireNonNull(coordinates);
    }

    /**
     * Creates a {@link GeoLocation} object.
     *
     * @param destination represents a user's {@link TripDestination} provided to the chatbot.
     * @param fetchedLocation represents info fetched from a geocoding service for a {@link TripDestination}.
     * @return a {@link GeoLocation} object.
     */
    public static GeoLocation of(TripDestination destination, LocationResult.FetchedLocation fetchedLocation) {
        return new GeoLocation(destination, Coordinates.fromObject(fetchedLocation));
    }
}
