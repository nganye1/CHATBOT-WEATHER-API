package main.java.weather.data;

/**
 * Represent a data object to store information about the user's trip.
 *
 * @param city represents the city
 * @param country represents the country where the city is located
 * @param startDate represents the start date of the trip in the iso8601 date format i.e., YYYY-MM-DD
 * @param endDate represents the end date of the trip in the iso8601 date format i.e., YYYY-MM-DD
 *
 * @author Kelvin Kavisi
 */
public record TripDestination(String city, String country, String startDate, String endDate) {
    /**
     * Obtains the locality in the format of "city, country" i.e. Cork, Ireland
     * @return the locality of this destination.
     */
    public String getLocality() {
        return city + ", " + country;
    }
}
