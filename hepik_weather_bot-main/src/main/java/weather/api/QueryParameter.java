package main.java.weather.api;

/**
 * Represents a data class for adding query parameters when creating the {@link java.net.URI} object.
 *
 * @param key denotes the query parameter's key
 * @param value denotes the query parameter's value
 */
public record QueryParameter(String key, String value) {}
