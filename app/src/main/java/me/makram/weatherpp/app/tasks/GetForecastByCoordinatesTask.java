package me.makram.weatherpp.app.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import me.makram.weatherpp.app.activities.MainActivity;
import me.makram.weatherpp.app.backend.Day;
import me.makram.weatherpp.app.backend.ForecastRetriever;
import okhttp3.OkHttpClient;

/**
 * A task to retrieve the forecast asynchronously so that the main
 * thread is not disturbed.
 */
public class GetForecastByCoordinatesTask extends AsyncTask<Double, Void, List<Day>> {
    private static final String TAG = GetForecastByCoordinatesTask.class.getName();

    private OkHttpClient client;
    private MainActivity mainActivity;

    public GetForecastByCoordinatesTask(MainActivity activity) {
        this.client = activity.getOkHttpClient();
        this.mainActivity = activity;
    }

    @Override
    protected List<Day> doInBackground(Double... params) {
        List<Day> days = null;
        try {
            days = ForecastRetriever.retrieveForecast(params[0], params[1], client);
        } catch (IOException e) {
            Log.d(TAG, "Exception while retrieving forecast: " + e.getMessage());
        }

        return days;
    }

    @Override
    protected void onPostExecute(List<Day> days) {
        if (days != null) {
            mainActivity.populateForecastList(days);
        } else {
            Log.d(TAG, "Days is null");
        }
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Fetching forcast via longitude and latitude");
    }
}
