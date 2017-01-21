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

  private static final DecimalFormat DECIMAL_FORMAT_DISTANCE_WO_DECIMALS = new DecimalFormat("#");

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
    return formatDistanceWithDecimals(value, true);
  }

  /**
   * formats the given value like a distance value
   *
   * @param value
   *  the value to be formatted
   * @param decimalFlag
   *  whether or not one decimal should be present
   * @return the formatted value with optionally one decimal
   */
  public static String formatDistanceWithDecimals(double value, boolean decimalFlag) {
    String result;
    if( decimalFlag ) {
      result = DECIMAL_FORMAT_DISTANCE.format(value);
    } else {
      result = DECIMAL_FORMAT_DISTANCE_WO_DECIMALS.format(value);
    }
    return result;
  }

}
