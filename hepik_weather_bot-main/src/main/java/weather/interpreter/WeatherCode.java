package main.java.weather.interpreter;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents the weather code info sent back by OpenMeteo.
 *
 * @author Kelvin Kavisi
 * @author Peter Nganye Yakura
 */
public enum WeatherCode {
    UNKNOWN(-1),
    CLEAR_SKY(0),
    MAINLY_CLEAR(1),
    PARTLY_CLOUDY(2),
    OVERCAST(3),
    FOG(45),
    DEPOSITING_RIME_FOG(48),
    LIGHT_DRIZZLE(51),
    MODERATE_DRIZZLE(53),
    DENSE_DRIZZLE(55),
    LIGHT_FREEZING_DRIZZLE(56),
    DENSE_FREEZING_DRIZZLE(57),
    SLIGHT_RAIN(61),
    MODERATE_RAIN(63),
    HEAVY_RAIN(65),
    LIGHT_FREEZING_RAIN(66),
    HEAVY_FREEZING_RAIN(67),
    SLIGHT_SNOWFALL(71),
    MODERATE_SNOWFALL(73),
    HEAVY_SNOWFALL(75),
    SNOW_GRAINS(77),
    SLIGHT_RAIN_SHOWERS(80),
    MODERATE_RAIN_SHOWERS(81),
    VIOLENT_RAIN_SHOWERS(82),
    SLIGHT_SNOW_SHOWERS(85),
    HEAVY_SNOW_SHOWERS(86),
    THUNDERSTORM(95),
    THUNDERSTORM_WITH_SLIGHT_HAIL(96),
    THUNDERSTORM_WITH_HEAVY_HAIL(99);

    /**
     * Represents the integer weather code associated an enum.
     */
    private final int weatherCode;

    WeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    /**
     * Obtains the {@link WeatherCode} object representing the weather code info sent back by OpenMeteo.
     *
     * @param weatherCode an integer representing a standard WMO code
     * @return {@link WeatherCode} object
     */
    public static WeatherCode of(int weatherCode) {
        return Arrays.stream(values())
                .filter(element -> element.weatherCode == weatherCode)
                .findFirst()
                .orElse(WeatherCode.UNKNOWN);
    }

    /**
     * Returns a brief description of the weather condition for a given day.
     *
     * @return a String describing the weather condition for the day
     */
    @Override
    public String toString() {
        return "Expect " + switch (this) {
            case CLEAR_SKY -> "a clear sky with no clouds in sight.";
            case MAINLY_CLEAR -> "a mainly clear sky with a few scattered clouds.";
            case PARTLY_CLOUDY -> "a partly cloudy sky.";
            case OVERCAST -> "a cloudy sky.";
            case FOG -> "a day characterized by thick fog, reducing visibility significantly.";
            case DEPOSITING_RIME_FOG -> "a foggy day with deposition of rime forming frosty crystals.";
            case LIGHT_DRIZZLE -> "a day with light drizzle.";
            case MODERATE_DRIZZLE -> "a drizzly day with slightly heavy raindrops.";
            case DENSE_DRIZZLE -> "a drizzly day with consistently heavy raindrops.";
            case LIGHT_FREEZING_DRIZZLE -> "a day with light freezing drizzle causing icy conditions.";
            case DENSE_FREEZING_DRIZZLE -> "a day with dense freezing drizzle leading to hazardous icy surfaces.";
            case SLIGHT_RAIN -> "a day with continuous light rain.";
            case MODERATE_RAIN -> "a day with steady rainfall.";
            case HEAVY_RAIN -> "a day with heavy rain and intense downpour.";
            case LIGHT_FREEZING_RAIN -> "a day with light freezing rain creating icy surfaces.";
            case HEAVY_FREEZING_RAIN -> "a day with heavy freezing rain causing hazardous icy conditions.";
            case SLIGHT_SNOWFALL -> "a day with light snowfall.";
            case MODERATE_SNOWFALL -> "a day with steady snowfall rates.";
            case HEAVY_SNOWFALL -> "a day with intense snowfall rates.";
            case SNOW_GRAINS -> "a day characterized by snow grains, tiny icy particles falling from the sky.";
            case SLIGHT_RAIN_SHOWERS -> "a day with brief periods of rain.";
            case MODERATE_RAIN_SHOWERS -> "a day with brief periods of steady rainfall.";
            case VIOLENT_RAIN_SHOWERS -> "a day with brief periods of intense and heavy rainfall.";
            case SLIGHT_SNOW_SHOWERS -> "a day with brief periods of snowfall.";
            case HEAVY_SNOW_SHOWERS -> "a day with brief periods of intense and heavy snowfall.";
            case THUNDERSTORM -> "a thunderstorm with lightning, thunder, and heavy rain.";
            case THUNDERSTORM_WITH_SLIGHT_HAIL ->
                    "a thunderstorm with slight hail, accompanied by lightning and thunder.";
            case THUNDERSTORM_WITH_HEAVY_HAIL ->
                    "a thunderstorm with heavy hail, accompanied by lightning and thunder.";
            default -> "an unpredictable weather condition.";
        };
    }
}
