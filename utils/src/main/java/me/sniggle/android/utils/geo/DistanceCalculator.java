package me.sniggle.android.utils.geo;

/**
 * Convenience class to calculate distances between two GPS coordinates
 */
public final class DistanceCalculator {

  /**
   * enumeration of unit systems
   */
  public enum Unit {
    METRIC("SI"),
    US("US");

    private String preferenceValue;

    Unit(String preferenceValue) {
      this.preferenceValue = preferenceValue;
    }

    /**
     * factory method to create a Unit system from the text values
     *
     * @param preferenceValue
     *  the string representation
     * @return the unit
     */
    public static Unit fromPreferenceValue(String preferenceValue) {
      Unit result = Unit.METRIC;
      for( Unit u : values() ) {
        if( u.preferenceValue.equals(preferenceValue) ) {
          result = u;
        }
      }
      return result;
    }
  }

  /**
   * the circumference of the earth in meters
   */
  private static final long R = 6_371_000;

  /**
   * calculates the distance between two locations
   *
   * @param latitudeA
   *   the latitude of the first coordinate
   * @param longitudeA
   *   the longitude of the first coordinate
   * @param latitudeB
   *   the latitude of the second coordinate
   * @param longitudeB
   *   the longitude of the second coordinate
   * @return the distance in meters
   */
  public static double distance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
    double phiOne = Math.toRadians(latitudeA);
    double phiTwo = Math.toRadians(latitudeB);
    double deltaPhi = Math.toRadians(latitudeB - latitudeA);
    double deltaLambda = Math.toRadians(longitudeB - longitudeA);

    double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
        Math.cos(phiOne) * Math.cos(phiTwo) *
            Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return  R * c;
  }

  /**
   * calculates the distance between the coordinates and formats it smartly
   *
   * @param a
   *   the first coordinate
   * @param b
   *   the second coordinate
   * @param unit
   *   the unit system to use
   * @return the smartly formatted distance string
   */
  public static String calculateDistance(Coords a, Coords b, Unit unit ) {
    return calculateDistance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude(), unit);
  }

  /**
   * calculates the distance between the coordinates and formats it smartly
   *
   * @param latitudeA
   *   the latitude of the first coordinate
   * @param longitudeA
   *   the longitude of the first coordinate
   * @param latitudeB
   *   the latitude of the second coordinate
   * @param longitudeB
   *   the longitude of the second coordinate
   * @param unit
   *   the unit system to use
   * @return the smartly formatted distance string
   */
  public static String calculateDistance(double latitudeA, double longitudeA, double latitudeB, double longitudeB, Unit unit) {
    StringBuffer result = new StringBuffer();
    double distanceInMeters = distance(latitudeA, longitudeA, latitudeB, longitudeB);
    double baseDistance = distanceInMeters;
    if( baseDistance > 0 ) {
      switch (unit) {
        case METRIC:
          if( baseDistance > 1000 ) {
            result.append(Utils.formatDistance(baseDistance / 1000.0)).append(" km");
          } else {
            result.append(Math.round(distanceInMeters)).append(" m");
          }
          break;
        case US:
          baseDistance = distanceInMeters / 0.3048; //conversion to feet
          if( baseDistance > 5280 ) {
            result.append(Utils.formatDistance(baseDistance/5280.0)).append(" mi");
          } else {
            result.append(Math.round(baseDistance) + " ft");
          }
          break;
      }
    } else {
      switch (unit) {
        case METRIC: result.append("0 m"); break;
        case US: result.append("0 ft"); break;
      }
    }
    return result.toString();
  }

}
