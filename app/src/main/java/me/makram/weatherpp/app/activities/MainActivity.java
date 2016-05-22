package me.makram.weatherpp.app.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import me.makram.weatherpp.app.AppController;
import me.makram.weatherpp.app.BuildConfig;
import me.makram.weatherpp.app.R;
import me.makram.weatherpp.app.backend.Day;
import me.makram.weatherpp.app.tasks.GetForecastByCoordinatesTask;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSION_LOCATION = 1;

    private GoogleApiClient googleApiClient;
    private OkHttpClient okHttpClient;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildGoogleApiClient();
        AppController application = (AppController) getApplication();
        okHttpClient = application.getClient();

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (lastLocation != null) {
            outState.putDouble("longitude", lastLocation.getLongitude());
            outState.putDouble("latitude", lastLocation.getLatitude());
        }
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
                        new GetForecastByCoordinatesTask(this).execute(lastLocation.getLongitude(),
                                lastLocation.getLatitude());
                    }
                } else {
                    Toast.makeText(this, "Not connected to the internet!", Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
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

    }
}
