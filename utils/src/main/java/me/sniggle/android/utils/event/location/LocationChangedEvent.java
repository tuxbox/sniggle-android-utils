package me.sniggle.android.utils.event.location;

import me.sniggle.android.utils.geo.Coords;

/**
 * Event to notify the application of a location change
 */
public final class LocationChangedEvent {

  private final Coords position;

  /**
   *
   * @param position
   *  the Geo Position
   */
  public LocationChangedEvent(Coords position) {
    this.position = position;
  }

  /**
   *
   * @return the Geo Position
   */
  public Coords getPosition() {
    return position;
  }

}
