package me.makram.weatherpp.app;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;

import me.makram.weatherpp.app.activities.DayActivity;
import me.makram.weatherpp.app.activities.MainActivity;
import me.makram.weatherpp.app.backend.Day;

/**
 * Created by admin on 5/22/16.
 */
public class DayForecastClickedListener implements AdapterView.OnItemClickListener {

    public static final String DAY_KEY = "dayForecast";

    private WeatherAdapter weatherAdapter;
    private MainActivity mainActivity;

    public DayForecastClickedListener(WeatherAdapter adapter, MainActivity activity) {
        this.mainActivity = activity;
        this.weatherAdapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Day day = (Day) weatherAdapter.getItem(position);

        Intent intent = new Intent(mainActivity, DayActivity.class);
        Gson gson = new Gson();
        String dayJson = gson.toJson(day, Day.class);
        intent.putExtra(DAY_KEY, dayJson);

        mainActivity.startActivity(intent);
    }
}
