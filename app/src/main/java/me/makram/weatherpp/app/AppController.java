package me.makram.weatherpp.app;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Extend the Application class to provide an environment
 * to create the OkHttpClient object (though this may not
 * be enough if we want to make a service).
 */
public class AppController extends Application {
    private OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
