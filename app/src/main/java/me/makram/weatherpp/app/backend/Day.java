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
    public double minTemp;
    public double maxTemp;

    /**
     * Temperatures at specific times of the day.
     */
    public double dayTemp;
    public double eveTemp;
    public double mornTemp;
    public double nightTemp;

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
    public double pressure;
    /**
     * An icon to display to the user depending on the weather.
     */
    public String icon;
    /**
     * The city name.
     */
    public String cityName;
    /**
     * The country name.
     */
    public String countryName;
    /**
     * Internal id used by openweathermap, may be useful in the future.
     */
    public int locationId;

    public String toString() {
        return "{\n" +
                "minTemp: " + minTemp + ",\n" +
                "maxTemp: " + maxTemp + ",\n" +
                "dayTemp: " + dayTemp + ",\n" +
                "eveTemp: " + eveTemp + ",\n" +
                "mornTemp: " + mornTemp + ",\n" +
                "nightTemp: " + nightTemp + ",\n" +
                "description: " + description + ",\n" +
                "main: " + main + ",\n" +
                "humidity: " + humidity + ",\n" +
                "pressure: " + pressure + ",\n" +
                "icon: " + icon + ",\n" +
                "city: " + cityName + "\n" +
                "}";
    }
}
