package me.sniggle.android.utils.service.handler;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.squareup.otto.Subscribe;

import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.event.location.GetLastLocationEvent;
import me.sniggle.android.utils.event.location.GetLocationEvent;
import me.sniggle.android.utils.event.location.LocationChangedEvent;
import me.sniggle.android.utils.event.location.PauseLocationRequestEvent;
import me.sniggle.android.utils.event.location.StartLocationRequestEvent;
import me.sniggle.android.utils.geo.Coords;

/**
 * A simple location handler implementation that deals with the main requirements
 */
public class LocationHandler<Ctx extends BaseContext> extends BaseEventHandler<Ctx> implements LocationListener {

  //5 minutes
  private long minimumTimeDifferenceBetweenUpdates = 300_000;
  private float minimumDistanceDifferenceBetweenUpdates = 100.0f;
  protected Coords lastLocation;
  protected LocationManager locationManager;

  /**
   * constructor
   *
   * @param appContext the main app dependency context
   */
  public LocationHandler(Ctx appContext) {
    super(appContext);
  }

  /**
   *
   * @return the minimum distance that triggers a continuous location listener update in meteres
   */
  public float getMinimumDistanceDifferenceBetweenUpdates() {
    return minimumDistanceDifferenceBetweenUpdates;
  }

  /**
   *
   * @param minimumDistanceDifferenceBetweenUpdates
   *   the minimum distance that triggers a continuous location listener update in meteres
   */
  public void setMinimumDistanceDifferenceBetweenUpdates(float minimumDistanceDifferenceBetweenUpdates) {
    this.minimumDistanceDifferenceBetweenUpdates = minimumDistanceDifferenceBetweenUpdates;
  }

  /**
   *
   * @return the minimum time difference that triggers a continuous location listener update in milliseconds
   */
  public long getMinimumTimeDifferenceBetweenUpdates() {
    return minimumTimeDifferenceBetweenUpdates;
  }

  /**
   *
   * @param minimumTimeDifferenceBetweenUpdates
   *    the minimum time difference that triggers a continuous location listener update in milliseconds
   */
  public void setMinimumTimeDifferenceBetweenUpdates(long minimumTimeDifferenceBetweenUpdates) {
    this.minimumTimeDifferenceBetweenUpdates = minimumTimeDifferenceBetweenUpdates;
  }

  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      publishEvent(new LocationChangedEvent(new Coords(location.getLatitude(), location.getLongitude())));
    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }

  @Override
  public void onCreate() {
    super.onCreate();
    locationManager = (LocationManager) getAppContext().getContext().getSystemService(Context.LOCATION_SERVICE);
  }

  @Override
  public void onDestroy() {
    locationManager = null;
    super.onDestroy();
  }

  /**
   * checks for permissions and if granted performs a single location update request using the given provider
   *
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   * @param provider
   *    the provider to use for single location request
   */
  protected void requestSingleLocation(String provider) {
    if (locationManager != null) {
      if (ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
          ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        locationManager.requestSingleUpdate(provider, this, null);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minimumTimeDifferenceBetweenUpdates, minimumDistanceDifferenceBetweenUpdates, this);
      }
    }
  }

  /**
   * checks the permissions and registers a continuous location update listener
   *
   * @see #getMinimumDistanceDifferenceBetweenUpdates()
   * @see #getMinimumTimeDifferenceBetweenUpdates()
   * @see LocationManager#GPS_PROVIDER
   * @see LocationManager#NETWORK_PROVIDER
   * @see LocationManager#PASSIVE_PROVIDER
   * @param provider
   *    the provider to use
   */
  protected void registerContinuousLocationListeners(String provider) {
    if (locationManager != null) {
      if (ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
          ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minimumTimeDifferenceBetweenUpdates, minimumDistanceDifferenceBetweenUpdates, this);
      }
    }
  }

  /**
   * removes the LocationHandler as Location Update Listener
   */
  protected void unregisterLocationListeners() {
    if( locationManager != null ) {
      if (ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
          ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        locationManager.removeUpdates(this);
      }
    }
  }

  @Subscribe
  public void handleStartLocationRequestEvent(StartLocationRequestEvent event) {
    requestSingleLocation(event.getSingleLocationRequestProvider());
    registerContinuousLocationListeners(event.getContinuousLocationRequestProvider());
  }

  @Subscribe
  public void handlePauseLocationRequestEvent(PauseLocationRequestEvent event) {
    unregisterLocationListeners();
  }

  @Subscribe
  public void handleGetLastLocationEvent(GetLastLocationEvent event) {
    if (lastLocation != null) {
      publishEvent(new LocationChangedEvent(lastLocation));
    } else {
      LocationManager locationManager = (LocationManager) getAppContext().getContext().getSystemService(Context.LOCATION_SERVICE);
      if (ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getAppContext().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if( lastKnownLocation == null ) {
          lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
          if( lastKnownLocation == null ) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
          }
        }
        if( lastKnownLocation == null ) {
          publishEvent(new GetLocationEvent());
        } else {
          lastLocation = new Coords(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }
      }
    }
  }

  @Subscribe
  public void handleGetLocationEvent(GetLocationEvent event) {
    requestSingleLocation(event.getProvider());
  }

}
