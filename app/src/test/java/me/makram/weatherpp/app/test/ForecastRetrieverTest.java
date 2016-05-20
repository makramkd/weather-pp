package me.makram.weatherpp.app.test;

import org.junit.Test;

import java.util.List;

import static me.makram.weatherpp.app.backend.ForecastRetriever.retrieveForecast;

import me.makram.weatherpp.app.backend.Day;
import okhttp3.OkHttpClient;

import static org.junit.Assert.*;

/**
 * Created by admin on 5/20/16.
 */
public class ForecastRetrieverTest {
    OkHttpClient client = new OkHttpClient();

    @Test
    public void testRetrieveForecast() throws Exception {
        List<Day> days = retrieveForecast(-0.12574, 51.50853, client);
        for (Day day : days) {
            System.out.println(day.toString());
        }
    }

    @Test
    public void testRetrieveForecast1() throws Exception {

    }
}