package main.java.phpBridge;

/**
 * Represents an object with information about the request made from PHP via the shell.
 *
 * @param target denotes the {@link PhpRequestTarget} of the shell call. Usually, a location/weather request.
 * @param input denotes the input required for the targeted request.
 */
public record PhpRequest(PhpRequestTarget target, String input) {}
