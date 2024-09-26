package tests.java.weather.interpreter;

import main.java.weather.data.Forecast;
import main.java.weather.data.WeatherForecast;
import main.java.weather.interpreter.WeatherCode;
import main.java.weather.interpreter.WeatherInterpreter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static tests.java.weather.TestHelper.getInterpretation;
import static tests.java.weather.TestHelper.getTestWeather;

public class WeatherInterpreterTest {
    private final String DEFAULT_DATE = "2024-04-15";

    @Test
    void testClearSkyRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.CLEAR_SKY);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        System.out.println(getInterpretation(forecast));

        System.out.println(interpretation);


        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testPartlyCloudyRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.PARTLY_CLOUDY);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testOvercastRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.OVERCAST);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testFoggyRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.FOG);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testLightDrizzleRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.LIGHT_DRIZZLE);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testHeavyRainRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.HEAVY_RAIN);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testSnowfallRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.HEAVY_SNOWFALL);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testThunderstormRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.THUNDERSTORM);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testUnknownWeatherCodeRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.UNKNOWN);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testModerateDrizzleRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.MODERATE_DRIZZLE);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testDenseDrizzleRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.DENSE_DRIZZLE);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testLightFreezingDrizzleRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.LIGHT_FREEZING_DRIZZLE);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testDenseFreezingDrizzleRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.DENSE_FREEZING_DRIZZLE);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testLightFreezingRainRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.LIGHT_FREEZING_RAIN);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testHeavyFreezingRainRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.HEAVY_FREEZING_RAIN);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testSnowGrainsRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.SNOW_GRAINS);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testSlightSnowShowersRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.SLIGHT_SNOW_SHOWERS);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testHeavySnowShowersRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.HEAVY_SNOW_SHOWERS);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }

    @Test
    void testThunderstormWithSlightHailRecommendations() {
        WeatherForecast weatherForecast = getTestWeather(WeatherCode.THUNDERSTORM_WITH_SLIGHT_HAIL);
        Forecast forecast = weatherForecast.getForecast(DEFAULT_DATE);
        String interpretation = WeatherInterpreter.interpretWeather(weatherForecast).get(DEFAULT_DATE);

        assertEquals(getInterpretation(forecast), interpretation);
    }
}
