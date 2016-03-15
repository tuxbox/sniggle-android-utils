package me.sniggle.android.utils.event.permission;

/**
 * Event indication that a permission was granted, providing the application specific request code
 */
public class PermissionGrantedEvent {

  private final int requestCode;

  public PermissionGrantedEvent(int requestCode) {
    this.requestCode = requestCode;
  }

  public int getRequestCode() {
    return requestCode;
  }

}
