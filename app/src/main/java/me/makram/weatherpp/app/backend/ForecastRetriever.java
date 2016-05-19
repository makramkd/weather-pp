package me.makram.weatherpp.app.backend;

import java.util.List;

/**
 * The class that makes API requests to the
 * Open Weather API, using our API key.x
 */
public class ForecastRetriever {

    /**
     * Retrieve the forecast given the longitude and latitude, which is
     * provided by the device's GPS.
     * @param longitude
     * @param latitude
     * @return A list of Day objects, which corresponds to a forecast.
     */
    public static List<Day> retrieveForecast(float longitude, float latitude) {
        return null; // TODO: implement this method
    }

    /**
     * Retrieve the forecast given a custom query string, which could be a postal
     * code.
     * @param customQuery
     * @return A list of Day objects, which corresponds to a forecast.
     */
    public static List<Day> retrieveForecast(String customQuery){
        return null; // TODO: implement this method
    }
}
