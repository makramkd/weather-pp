package me.makram.weatherpp.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import me.makram.weatherpp.app.AppController;
import me.makram.weatherpp.app.BuildConfig;
import me.makram.weatherpp.app.DayForecastClickedListener;
import me.makram.weatherpp.app.R;
import me.makram.weatherpp.app.WeatherAdapter;
import me.makram.weatherpp.app.backend.Day;
import me.makram.weatherpp.app.tasks.GetForecastByCoordinatesTask;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSION_LOCATION = 1;

    private GoogleApiClient googleApiClient;
    private OkHttpClient okHttpClient;
    private Location lastLocation;

    private WeatherAdapter weatherAdapter;
    private ListView listView;
    private DayForecastClickedListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildGoogleApiClient();
        AppController application = (AppController) getApplication();
        okHttpClient = application.getClient();

        // initially empty weather adapter.
        weatherAdapter = new WeatherAdapter(this, R.layout.forecast_item, new ArrayList<Day>(),
                application.getImageLoader());
        listener = new DayForecastClickedListener(weatherAdapter, this);
        listView = (ListView) findViewById(R.id.listView);
        // set the adapter
        if (listView != null) {
            listView.setAdapter(weatherAdapter);
            listView.setOnItemClickListener(listener);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: save current forecast list in the bundle/preferences
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        // TODO: check if we cannot get proper location in order
        // to use previous location in bundle.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                if (networkIsAvailable()) {
                    if (lastLocation != null) {
                        new GetForecastByCoordinatesTask(this, swipeRefreshLayout).execute(lastLocation.getLongitude(),
                                lastLocation.getLatitude());
                    }
                } else {
                    Toast.makeText(this, "Not connected to the internet!", Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int locationPermission =
                            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Location permission granted: " + (locationPermission ==
                        PackageManager.PERMISSION_GRANTED));
                    }
                    lastLocation =
                            LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                }
            default:
                break;
        }
    }

    /**
     * Return true if the device is connected to the internet.
     * @return true if the device is connected to the internet.
     */
    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Runs when the GoogleApiClient object successfully connects.
     * @param connectionHint the connection hint
     */
    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        int locationPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation == null) {
                Log.d(TAG, "lastLocation is null");
            }
        } else {
            // need to request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended. Cause: " + cause);
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Reason: " + connectionResult.getErrorMessage());
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * Populate the ListView in the activity with the given forecasts.
     * @param days
     */
    public void populateForecastList(List<Day> days) {
        weatherAdapter.setDays(days);
    }

    @Override
    public void onRefresh() {
        if (networkIsAvailable()) {
            if (lastLocation != null) {
                new GetForecastByCoordinatesTask(this, swipeRefreshLayout).execute(lastLocation.getLongitude(),
                        lastLocation.getLatitude());
            } else {
                Log.d(TAG, "lastLocation is null");
            }
        } else {
            Toast.makeText(this, "Network is not available: please connect to the internet",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
