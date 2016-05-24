package me.makram.weatherpp.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import me.makram.weatherpp.app.DayForecastClickedListener;
import me.makram.weatherpp.app.R;
import me.makram.weatherpp.app.backend.Day;

public class DayActivity extends AppCompatActivity {
    private static final String TAG = DayActivity.class.getName();

    private Day day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        String dayString = extras.getString(DayForecastClickedListener.DAY_KEY, "null");
        if (dayString.equals("null")) {
            Log.d(TAG, "Day key is null!");
        }
        day = gson.fromJson(extras.getString(DayForecastClickedListener.DAY_KEY), Day.class);
    }
}
