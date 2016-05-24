package me.makram.weatherpp.app.tasks;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

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
    private SwipeRefreshLayout swipeRefreshLayout;

    public GetForecastByCoordinatesTask(MainActivity activity, SwipeRefreshLayout swipeRefreshLayout) {
        this.client = activity.getOkHttpClient();
        this.mainActivity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
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
        swipeRefreshLayout.setRefreshing(false);
        if (days != null) {
            mainActivity.populateForecastList(days);
            Toast.makeText(mainActivity, "Weather data refreshed!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Days is null");
        }
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Fetching forcast via longitude and latitude");
        swipeRefreshLayout.setRefreshing(true);
    }
}
