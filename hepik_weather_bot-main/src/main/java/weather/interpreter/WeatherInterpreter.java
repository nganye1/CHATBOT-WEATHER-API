package main.java.weather.interpreter;

import main.java.weather.data.Forecast;
import main.java.weather.data.WeatherForecast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a class for interpreting weather forecasts and suggesting clothing.
 *
 * @author Kelvin Kavisi
 * @author Peter Nganye Yakura
 * @author Harshiv Bharat Patel
 */

public class WeatherInterpreter {

    /**
     * Interprets the weather forecast and suggests clothing.
     *
     * @param weatherForecast The weather forecast to interpret.
     * @return The clothing suggestions based on the weather forecast.
     */
    public static Map<String, String> interpretWeather(WeatherForecast weatherForecast) {
        Map<String, String> interpretationMap = new LinkedHashMap<>();
        String[] availableDates = weatherForecast.getAvailableDates();

        for (String date : availableDates) {
            Forecast forecast = weatherForecast.getForecast(date);
            String interpretation = generateInterpretation(forecast) + " " + generateOutfitRecommendations(forecast);
            interpretationMap.put(date, interpretation);
        }

        // Present all outfit recommendation
        return interpretationMap;
    }

    public static String generateInterpretation(Forecast forecast) {
        return forecast.weatherCode().toString() +

                // Add temperature
                " The temperature is expected to rise to a maximum of " +
                forecast.maxTemperature() +
                "°C that will feel like " +
                forecast.maxApparentTemperature() + "°C " +
                "and drop to a minimum of " +
                forecast.minTemperature() + "°C " +
                "that will feel like " +
                forecast.minApparentTemperature() + "°C. " +

                // Add daylight and sunshine duration interpretation
                "At most, " +
                Math.round(forecast.getSunshineToDaylightRatio()) +
                "% of the day will experience some level of sunshine with a " +

                // Add precipitation probability interpretation
                Math.round(forecast.precipitationProbability()) +
                "% chance of precipitation and " +

                // Add wind speed interpretation
                "maximum wind speeds of " +
                forecast.windSpeed() +
                " km/hr.";
    }

    public static String generateOutfitRecommendations(Forecast forecast) {
        return switch (forecast.weatherCode()) {
            case CLEAR_SKY -> "A good day for your sunglasses.";
            case PARTLY_CLOUDY, MAINLY_CLEAR -> "Dress in layers as the weather may change.";
            case OVERCAST -> "Light layers should suffice.";
            case FOG, DEPOSITING_RIME_FOG -> "Wear bright colors for visibility.";
            case LIGHT_DRIZZLE, MODERATE_DRIZZLE, DENSE_DRIZZLE ->
                    "A light raincoat or a water-repellent hat is recommended.";
            case LIGHT_FREEZING_DRIZZLE, DENSE_FREEZING_DRIZZLE ->
                    "Insulate well and ensure your footing with non-slip boots.";
            case SLIGHT_RAIN, MODERATE_RAIN, HEAVY_RAIN -> "Waterproof clothing and shoes are a must.";
            case LIGHT_FREEZING_RAIN, HEAVY_FREEZING_RAIN -> "Wear thermal layers and be careful of icy conditions.";
            case SLIGHT_SNOWFALL, MODERATE_SNOWFALL, HEAVY_SNOWFALL ->
                    "Warm layers and a coat, along with a hat, gloves, and boots.";
            case SNOW_GRAINS -> "Dress warmly and consider traction for your footwear.";
            case SLIGHT_RAIN_SHOWERS, MODERATE_RAIN_SHOWERS, VIOLENT_RAIN_SHOWERS ->
                    "Quick-dry fabrics and an umbrella would be wise.";
            case SLIGHT_SNOW_SHOWERS, HEAVY_SNOW_SHOWERS -> "Insulated layers and waterproof boots are recommended.";
            case THUNDERSTORM, THUNDERSTORM_WITH_SLIGHT_HAIL, THUNDERSTORM_WITH_HEAVY_HAIL ->
                    "Stay indoors if possible. If outside, avoid open fields and tall objects.";
            default -> "Stay prepared for changes and check the latest weather reports.";
        };
    }
}