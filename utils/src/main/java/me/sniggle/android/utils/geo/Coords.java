package me.sniggle.android.utils.geo;

/**
 * A coordination container
 */
public final class Coords {

  private final double latitude;
  private final double longitude;

  public Coords(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

}
