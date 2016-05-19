package me.makram.weatherpp.app.backend;

/**
 * A class that represents the weather information we want
 * to display to the user for a single day.
 *
 * A forecast would be a collection of Day objects.
 */
public class Day {

    /**
     * Min and max temperatures of the day.
     */
    public int minTemp;
    public int maxTemp;

    /**
     * Temperatures at specific times of the day.
     */
    public int dayTemp;
    public int eveTemp;
    public int mornTemp;
    public int nightTemp;

    /**
     * A description of the day: i.e "broken clouds"
     */
    public String description;
    /**
     * Main aspect of the day: i.e "Cloudy", "Sunny"
     */
    public String main;
    /**
     * Humidity as a percentage.
     */
    public int humidity;
    /**
     * Pressure in hectopascals.
     */
    public float pressure;

}
