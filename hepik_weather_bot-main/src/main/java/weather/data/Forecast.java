package main.java.weather.data;

import main.java.weather.data.json_bindings.ForecastResult;
import main.java.weather.interpreter.WeatherCode;

/**
 * Represents a data object for storing the weather forecast fetched for {@link TripDestination}.
 *
 * @param weatherCode represents a standard WMO code for the general weather condition of a location
 * @param maxTemperature represents the maximum expected daily air temperature at 2 meters above ground
 * @param minTemperature represents the minimum expected daily air temperature at 2 meters above ground
 * @param maxApparentTemperature represents the maximum apparent temperature.
 *                               Typically less than the maxTemperature and commonly known as "feels-like".
 * @param minApparentTemperature represents the minimum apparent temperature.
 *                               Typically less than the minTemperature and commonly known as "feels-like"
 * @param daylightDuration represents the number of seconds regarded as daylight per day
 * @param sunshineDuration represents the number of seconds with sunshine per day.
 *                         Sunshine duration will consistently be less than daylight duration due to dawn and dusk.
 * @param precipitationProbability represents the probability of occurrence for any form of precipitation i.e.,
 *                                 rain, snow, showers.
 * @param windSpeed represents the speed of wind in km/hr.
 *
 * @author Kelvin Kavisi
 */
public record Forecast(
        WeatherCode weatherCode,
        double maxTemperature,
        double minTemperature,
        double maxApparentTemperature,
        double minApparentTemperature,
        double daylightDuration,
        double sunshineDuration,
        double precipitationProbability,
        double windSpeed
) {

    /**
     * Calculate the percentage of the day that will have sunshine.
     *
     * @return percentage of day with sunshine.
     */
    public double getSunshineToDaylightRatio() {
        return (sunshineDuration / daylightDuration) * 100;
    }

    public static Forecast[] fromObject(ForecastResult.DailyForecast dailyForecast) {
        int count = dailyForecast.dates().length; // Count of forecasts by date

        Forecast[] forecasts = new Forecast[count];

        // Access frequently used arrays
        int[] weatherCodes = dailyForecast.weatherCodes();
        double[] maxTemperatures = dailyForecast.maxTemperatures();
        double[] minTemperatures = dailyForecast.minTemperatures();
        double[] maxApparentTemperatures = dailyForecast.maxApparentTemperatures();
        double[] minApparentTemperatures = dailyForecast.minApparentTemperatures();
        double[] daylightDurations = dailyForecast.daylightDurations();
        double[] sunshineDurations = dailyForecast.sunshineDurations();
        double[] precipitationProbabilities = dailyForecast.precipitationProbabilities();
        double[] windSpeeds = dailyForecast.windSpeeds();

        for (int i = 0; i < count; i++) {
            forecasts[i] = new Forecast(
                    WeatherCode.of(weatherCodes[i]),
                    maxTemperatures[i],
                    minTemperatures[i],
                    maxApparentTemperatures[i],
                    minApparentTemperatures[i],
                    daylightDurations[i],
                    sunshineDurations[i],
                    precipitationProbabilities[i],
                    windSpeeds[i]
            );

        }

        return forecasts;
    }
}
