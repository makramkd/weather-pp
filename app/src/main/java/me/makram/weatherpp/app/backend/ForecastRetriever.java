package me.makram.weatherpp.app.backend;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.makram.weatherpp.app.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The class that makes API requests to the
 * Open Weather API, using our API key.
 */
public class ForecastRetriever {

    /**
     * Constants needed for URI information.
     */
    public static final Uri FORECAST_API_URL =
            Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily");


    /**
     * Retrieve the forecast given the longitude and latitude, which is
     * provided by the device's GPS.
     *
     * This should be used within an AsyncTask or Service if possible.
     * @param longitude the longitude of the user device, usually retrieved via GPS.
     * @param latitude the latitude of the user device, usually retrieved via GPS.
     * @return A list of Day objects, which corresponds to a forecast.
     */
    public static List<Day> retrieveForecast(double longitude, double latitude, OkHttpClient client) throws IOException {
        Uri.Builder builder = FORECAST_API_URL.buildUpon()
                .appendQueryParameter("lat", Double.toString(latitude))
                .appendQueryParameter("lon", Double.toString(longitude))
                .appendQueryParameter("cnt", "7")
                .appendQueryParameter("APPID", BuildConfig.OPEN_WEATHER_API_KEY);
        Uri uri = builder.build();
        Request request = new Request.Builder()
                .url(uri.toString())
                .build();

        Response response = client.newCall(request).execute();

        return processJson(response.body().string());
    }

    /**
     * Processes the returned JSON from the API request to OpenWeatherMap.
     * We are primarily interested in the JSONArray called "list", which has
     * objects that contain weather and presusre information.
     * @param string the JSON returned by OpenWeatherMap as a string object.
     * @return a list of Day objects, which is supposed to be a forecast.
     */
    private static List<Day> processJson(String string) {
        List<Day> forecast = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(string);
            JSONArray listArray = object.getJSONArray("list");
            for (int i = 0; i < listArray.length(); ++i) {
                forecast.add(createDayObject(listArray, i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return forecast;
    }

    /**
     * Create the Day object from the given listArray, which is constructed from
     * the JSON retrieved by forecast16 API in the OpenWeatherMap API.
     * @param listArray the list array in the JSON
     * @param i the index into the list array we are currently processing
     * @return the Day object corresponding to the given index.
     * @throws JSONException
     */
    private static Day createDayObject(JSONArray listArray, int i) throws JSONException {
        JSONObject o = listArray.getJSONObject(i);
        JSONObject tempObject = o.getJSONObject("temp");
        JSONArray weatherArray = o.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        Day day = new Day();
        day.dayTemp = tempObject.getDouble("day");
        day.nightTemp = tempObject.getDouble("night");
        day.minTemp = tempObject.getDouble("min");
        day.maxTemp = tempObject.getDouble("max");
        day.eveTemp = tempObject.getDouble("eve");
        day.mornTemp = tempObject.getDouble("morn");
        day.main = weatherObject.getString("main");
        day.icon = weatherObject.getString("icon");
        day.description = weatherObject.getString("description");
        day.pressure = o.getDouble("pressure");
        day.humidity = o.getInt("humidity");

        return day;
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
