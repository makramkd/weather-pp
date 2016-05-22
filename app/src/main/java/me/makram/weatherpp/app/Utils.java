package me.makram.weatherpp.app;

/**
 * A utility class that contains some functions used
 * throughout the application.
 */
public class Utils {
    public static final double ABS_ZERO = -273.15;

    /**
     * Convert from kelvin to celsius.
     * @param kelvin the temperature in kelvin.
     * @return the temperature in celsius.
     */
    public static double kelvinToCelsius(double kelvin) {
        return kelvin + ABS_ZERO;
    }

    /**
     * Convert from celsius to fahrenheit.
     * @param celsius the temperature in celsius.
     * @return the temperature in fahrenheit
     */
    public static double celsiusToFahrenheit(double celsius) {
        return celsius * (9 / 5) + 32;
    }
}
