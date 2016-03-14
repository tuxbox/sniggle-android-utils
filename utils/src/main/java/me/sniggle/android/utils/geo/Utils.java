package me.sniggle.android.utils.geo;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Helper class
 */
public final class Utils {

  private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

  private static final DecimalFormat DECIMAL_FORMAT_CURRENCY = new DecimalFormat("#.##");

  private static final DecimalFormat DECIMAL_FORMAT_DISTANCE = new DecimalFormat("#.#");

  private Utils() {
  }

  /**
   * checks whether the given string is integer number
   *
   * @param s
   *  the string to check
   * @return true if its an integer
   */
  public static boolean isInteger(String s) {
    return s != null && NUMBER_PATTERN.matcher(s).matches();
  }

  /**
   * formats the given value like a currency value
   *
   * @param value
   *  the vale to formate
   * @return a number with at least one digit and exactly two decimals
   */
  public static String formatCurrency(double value) {
    return DECIMAL_FORMAT_CURRENCY.format(value);
  }

  /**
   * formats the given value like a distance value
   *
   * @param value
   *  the value to be formatted
   * @return a number with at least one digit and exactly one decimal
   */
  public static String formatDistance(double value) {
    return DECIMAL_FORMAT_DISTANCE.format(value);
  }

  /**
   * normalizes the given coordinate by offsetting latitude +90 and longitude +180
   * and multiplying the values by 100.
   *
   * @param a
   *  the coordinate to normalize
   * @return the normalized coordinate (latitude [45.55] + longitude [127.5] as "1355530750")
   */
  public static String normalizeCoordinates(Coords a) {
    return normalizeCoordinates(a.getLatitude(), a.getLongitude());
  }

  /**
   * normalizes the given coordinate by offsetting latitude +90 and longitude +180
   * and multiplying the values by 100.
   *
   * @param latitude
   *   the latitude
   * @param longitude
   *   the longitude
   * @return the normalized coordinate (latitude [45.55] + longitude [127.5] as "1355530750")
   */
  public static String normalizeCoordinates(double latitude, double longitude) {
    double normalizedLatitude = (latitude + 90.0)*100;
    double normalizedLongitude = (longitude + 180.0)*100;
    return String.format("%05d%05d", Math.round(normalizedLatitude), Math.round(normalizedLongitude));
  }


}
