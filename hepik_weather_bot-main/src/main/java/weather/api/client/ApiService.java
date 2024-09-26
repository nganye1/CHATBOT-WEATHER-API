package main.java.weather.api.client;

import main.java.weather.api.JsonExtractor;
import main.java.weather.api.QueryParameter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

/**
 * Represents a generic client that handles any HTTP client requests.
 *
 * @author Kelvin Kavisi
 */
public abstract class ApiService<T> {
    private final String apiURL;

    private final JsonExtractor<T> jsonExtractor = new JsonExtractor<>();

    public ApiService(String baseURL, String path) {
        String url = Objects.requireNonNull(baseURL);

        // Check if a slash is present
        if (url.charAt(url.length() - 1) != '/') url += '/';

        // Append path
        if (!path.isEmpty()) url += path;

        this.apiURL = url;
    }

    /**
     * Obtains the base URL of the API service.
     *
     * @return The base URL.
     */
    public String getApiURL() {
        return apiURL;
    }

    /**
     * Obtains the JSON extractor used for parsing JSON responses.
     *
     * @return The JSON extractor.
     */
    public JsonExtractor<T> getJsonExtractor() {
        return jsonExtractor;
    }

    /**
     * Sends an HTTP Request to a server.
     *
     * @param uris an array of {@link URI} to send requests to.
     * @return an {@link HttpResponse} based on the server's response.
     * @throws CompletionException when the underlying future failed with an error
     * @throws CancellationException when the underlying future was canceled.
     */
    protected HttpResponse<String>[] sendRequest(URI[] uris) throws CompletionException, CancellationException {
        if (uris.length == 0) return null;

        ArrayList<HttpResponse<String>> responses = new ArrayList<>();

        // Create HTTP requests for each URI
        List<HttpRequest> httpRequests = Arrays.stream(uris).map(uri -> HttpRequest.newBuilder(uri).build()).toList();

        try (HttpClient client = HttpClient.newHttpClient()) {

            // Take advantage of CPU, send all asynchronously. Also, reuse the HTTP client
            List<HttpResponse<String>> completedFutures = httpRequests
                    .parallelStream()
                    .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                    .toList()
                    .parallelStream()
                    .map(CompletableFuture::join)
                    .toList();

            responses.addAll(completedFutures);
        }

        return responses.isEmpty() ? null : responses.toArray(HttpResponse[]::new);
    }

    /**
     * Extracts and maps HTTP responses to their requisite object types.
     *
     * @param responses represents responses obtained from an asynchronous response to any API service.
     * @param anchorKey represents the key that MUST be present for the JSON to be mapped to an object.
     * @param tClass    denotes the Java Class type of the object that will hold the Java object.
     * @return {@link ApiResponse } that encapsulates the successful mapped objects and any exceptions encountered.
     */
    protected ApiResponse<T[], Integer> extractHttpResponses(HttpResponse<String>[] responses, String anchorKey, Class<T> tClass) {
        // Stores all successful results
        ArrayList<T> results = new ArrayList<>();

        ApiResponse<T[], Integer> apiResponse = new ApiResponse<>(); // Objects to embed both success and fails

        for (int i = 0; i < responses.length; i++) {
            try {
                // For each response, we extract the object. If null, we add generic errors.
                HttpResponse<String> response = responses[i];

                if (response.statusCode() == 200) {
                    T result = getJsonExtractor().extractFromJson(anchorKey, response.body(), tClass); // Map object

                    results.add(result);
                } else {
                    apiResponse.addError(i, "Encountered an HTTP error");
                }
            } catch (Exception ignored) {
                // Add generic error
                apiResponse.addError(i, "API response could not be processed");
            }
        }

        // Add successful results
        if (!results.isEmpty()) apiResponse.setSuccess((T[]) results.toArray(Object[]::new));

        return apiResponse.seal();
    }

    /**
     * Represents a static inner class for generating URIs for any {@link ApiService} subclass.
     */
    public static class UriBuilder {
        private final String url;

        public UriBuilder(String baseURL) {
            this.url = Objects.requireNonNull(baseURL);
        }

        /**
         * Generates a URI object for making HTTP requests.
         *
         * @param queryParameters denotes any query parameters to append to the base URL.
         * @return {@link URI} object.
         */
        public URI generateURI(QueryParameter... queryParameters) {
            StringBuilder uriBuilder = new StringBuilder(url); // Custom builder

            // Construct query parameters
            String queryParams = Arrays.stream(queryParameters)
                    .map(parameter -> {
                        String value = parameter.value().replaceAll(" " , "%20");

                        return String.format("%s=%s", parameter.key(), value);
                    })
                    .collect(Collectors.joining("&"));

            // Add all query parameters and create URI
            return URI.create(uriBuilder.append("?").append(queryParams).toString());
        }
    }

    /**
     * Creates a UriBuilder instance with the given URL for external use.
     *
     * @return {@link UriBuilder} instance.
     */
    public static UriBuilder createUriBuilder(String url) {
        return new UriBuilder(url);
    }

    /**
     * Creates a UriBuilder instance with the given URL for external use.
     *
     * @return {@link UriBuilder} instance.
     */
    protected UriBuilder getUriBuilder() {
        return new UriBuilder(apiURL);
    }
}