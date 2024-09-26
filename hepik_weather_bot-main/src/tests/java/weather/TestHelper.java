package tests.java.weather;

import main.java.weather.data.*;
import main.java.weather.interpreter.WeatherCode;
import main.java.weather.interpreter.WeatherInterpreter;
import main.java.weather.interpreter.WeatherCode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestHelper {
    public static TripDestination getTestDestination() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        return new TripDestination("Cork", "Ireland", date, date);
    }

    public static GeoLocation getTestLocation() {
        Coordinates coordinates = new Coordinates(51.89797, -8.47061, 10);
        return new GeoLocation(getTestDestination(), coordinates);
    }

    private static Forecast getForecast(WeatherCode weatherCode) {
        return new Forecast(weatherCode, 25.5, 15.0, 26.0, 20.5, 16.0 ,15.5 ,12.5,8.0);
    }

    public static WeatherForecast getTestWeather(WeatherCode weatherCode){
        GeoLocation location = TestHelper.getTestLocation(); // Assuming you have a method to get a test location
        String[] availableDates = {"2024-04-15"};

        return new WeatherForecast(location, availableDates, new Forecast[]{getForecast(weatherCode)});
    }

    public static String getInterpretation(Forecast forecast) {
        return WeatherInterpreter.generateInterpretation(forecast) +
                " " +
                WeatherInterpreter.generateOutfitRecommendations(forecast);
    }
}
