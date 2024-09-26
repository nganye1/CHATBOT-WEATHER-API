package main.java.weather.api.client;

import java.util.HashMap;
import java.util.Set;

/**
 * Represent a utility class that can store multiple exceptions incurred when fetching API responses asynchronously.
 * <p>
 * It is advisable to ensure there are no errors before accessing the success object encapsulated.
 *
 * @param <R> represents the Java type for success
 * @param <I> represents the Java type used for indexing the errors
 *
 * @author Kelvin Kavisi
 */
public class ApiResponse<R, I> {
    private boolean isSealed = false;

    private R success;

    private final HashMap<I, String> errors = new HashMap<>();

    /**
     * Accesses the success object encapsulated by this class.
     *
     * @return the success object encapsulated.
     */
    public R getSuccess() {
        return success;
    }


    /**
     * Checks if the response has a success object stored.
     *
     * @return true if a success has been set. Otherwise, false.
     */
    public boolean hasSuccess() {
        return success != null;
    }

    /**
     * Checks if the response has an error.
     *
     * @return true if an error is present. Otherwise, false.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Obtains a copy of the errors stored by this object.
     *
     * @return a HashMap of all indices linked to their errors.
     */
    public HashMap<I, String> getErrors() {
        return new HashMap<>(errors);
    }

    /**
     * Obtains all error indices stored by this object.
     *
     * @return a set of all indices with errors.
     */
    public Set<I> getIndicesWithErrors() {
        return getErrors().keySet();
    }

    /**
     * Adds a success result. Note this can only be done once. Nothing changes if this object has been sealed.
     *
     * @param success denotes object to set as the success.
     */
    public void setSuccess(R success) {
        // Set only once
        if (!hasSuccess() && !isSealed) this.success = success;
    }

    public String getError(I index) {
        return errors.get(index);
    }

    /**
     * Adds an error linked to an index. Nothing changes if this object has been sealed.
     *
     * @param index denotes the index to link to an error. If the index exists, it is replaced.
     * @param error denotes the error encountered from an API response.
     */
    public void addError(I index, String error) {
        if (!isSealed) errors.put(index, error);
    }

    /**
     * Seals the object and prevents any internal state from being modified.
     *
     * @return the current object in a totally unmodifiable state.
     */
    public ApiResponse<R, I> seal() {
        this.isSealed = true;
        return this;
    }
}
