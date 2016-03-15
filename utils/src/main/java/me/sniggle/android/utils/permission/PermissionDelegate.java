package me.sniggle.android.utils.permission;

import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;

/**
 * Simple interface that helps to delegate permission handling on Android 6 devices to central implementation
 */
public interface PermissionDelegate extends ActivityCompat.OnRequestPermissionsResultCallback {

  /**
   * ensures that the required permissions are provided or requests them as necessary
   */
  void ensurePermissions();

  /**
   * helper method to collect all permissions required that the #ensurePermissions() should check
   *
   * @param requestCode
   *  the permission request code
   * @param rationaleTextId
   *  the text id of the rationale to show
   * @param permissions
   *  the required permission
   * @return the delegate itself for chaining multiple permissions together
   */
  PermissionDelegate addPermission(int requestCode, @StringRes int rationaleTextId, String... permissions);

}
