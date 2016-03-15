package me.sniggle.android.utils.event.location;

import android.location.LocationManager;

/**
 * Event to request continuous location requests
 */
public final class StartLocationRequestEvent {

  private final String singleLocationRequestProvider;
  private final String continuousLocationRequestProvider;

  /**
   * default constructor falling back to GPS for single request and PASSIVE for continuous location request
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   */
  public StartLocationRequestEvent() {
    this(LocationManager.GPS_PROVIDER, LocationManager.PASSIVE_PROVIDER);
  }

  /**
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   *
   * @param continuousLocationRequestProvider
   *    the location provider to be used for continuous location requests
   * @param singleLocationRequestProvider
   *    the location provider to be used for a single location request
   */
  public StartLocationRequestEvent(String continuousLocationRequestProvider, String singleLocationRequestProvider) {
    this.continuousLocationRequestProvider = continuousLocationRequestProvider;
    this.singleLocationRequestProvider = singleLocationRequestProvider;
  }

  /**
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   *
   * @return the continuous location request provider
   */
  public String getContinuousLocationRequestProvider() {
    return continuousLocationRequestProvider;
  }

  /**
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   *
   * @return the single location request provider
   */
  public String getSingleLocationRequestProvider() {
    return singleLocationRequestProvider;
  }

}
