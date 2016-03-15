package me.sniggle.android.utils.event.location;

import android.location.LocationManager;

/**
 * Event to request the current location
 */
public final class GetLocationEvent {

  private final String provider;

  /**
   * Default constructor to use LocationManager#GPS_PROVIDER
   */
  public GetLocationEvent() {
    this(LocationManager.GPS_PROVIDER);
  }

  /**
   *
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   * @param provider
   *   the location provider to use
   */
  public GetLocationEvent(String provider) {
    this.provider = provider;
  }

  /**
   *
   * @return the location provider to fulfill this event
   */
  public String getProvider() {
    return provider;
  }
}
