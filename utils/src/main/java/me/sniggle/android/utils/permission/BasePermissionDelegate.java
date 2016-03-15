package me.sniggle.android.utils.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.squareup.otto.Bus;

import me.sniggle.android.utils.activity.BaseActivity;
import me.sniggle.android.utils.event.permission.PermissionGrantedEvent;

/**
 * Basic implementation of a permissions delegate
 */
public abstract class BasePermissionDelegate<Parent extends BaseActivity & ActivityCompat.OnRequestPermissionsResultCallback> implements PermissionDelegate {

  /**
   * helper class to bundle all permission related data together
   */
  private class PermissionGroup {
    private final int rationaleTextResourceId;
    private final int requestCode;
    private final String[] permissions;

    /**
     *
     * @param rationaleTextResourceId
     *  the text id to be used to show in the rationale text
     * @param requestCode
     *  the request code
     * @param permissions
     *  the required permissions
     */
    public PermissionGroup(int rationaleTextResourceId, int requestCode, String... permissions) {
      this.permissions = permissions;
      this.requestCode = requestCode;
      this.rationaleTextResourceId = rationaleTextResourceId;
    }

    /**
     *
     * @return the rationale text id
     */
    public int getRationaleTextResourceId() {
      return rationaleTextResourceId;
    }

    /**
     *
     * @return the request code
     */
    public int getRequestCode() {
      return requestCode;
    }

    /**
     *
     * @return the permissions
     */
    public String[] getPermissions() {
      return permissions;
    }
  }

  private final Parent parentActivity;
  private final SparseArray<PermissionGroup> permissions = new SparseArray<>();
  private final Bus eventBus;

  /**
   *
   * @param parentActivity
   *  the parent activity
   * @param eventBus
   *  the event bus
   */
  protected BasePermissionDelegate(Parent parentActivity, Bus eventBus) {
    this.parentActivity = parentActivity;
    this.eventBus = eventBus;
  }

  /**
   * internal method to ensure the permissions are granted
   *
   * @param requestCode
   *  the request code to be used to request permissions
   * @param rationaleTextId
   *  the rationale text id
   * @param permissions
   *  the required permissions
   */
  protected void ensurePermissions(int requestCode, int rationaleTextId, String[] permissions) {
    boolean showRationale = false;
    boolean requiredPermissions = true;
    for( String permission : permissions ) {
      showRationale |= ActivityCompat.shouldShowRequestPermissionRationale(parentActivity, permission);
      requiredPermissions &= ContextCompat.checkSelfPermission(parentActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }
    if( showRationale ) {
      showRationale(rationaleTextId);
    } else if( !requiredPermissions ) {
      ActivityCompat.requestPermissions(parentActivity, permissions, requestCode);
    }
  }

  /**
   * handles how to show the rationale message
   *
   * @param rationaleTextId
   *  the rationale text id
   */
  protected abstract void showRationale(int rationaleTextId);

  @Override
  public void ensurePermissions() {
    for( int i = 0; i < permissions.size(); i++ ) {
      PermissionGroup permissionGroup = permissions.valueAt(i);
      ensurePermissions(permissionGroup.getRequestCode(), permissionGroup.getRationaleTextResourceId(), permissionGroup.getPermissions());
    }
  }

  @Override
  public PermissionDelegate addPermission(int requestCode, @StringRes int rationaleTextId, String... permissions) {
    this.permissions.put(requestCode, new PermissionGroup(rationaleTextId, requestCode, permissions));
    return this;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    PermissionGroup permissionGroup = this.permissions.get(requestCode);
    if( permissionGroup != null ) {
      boolean permissionNotGranted = true;
      for( int i = 0; i < permissions.length; i++ ) {
        if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
          permissionNotGranted &= false;
        }
      }
      if( !permissionNotGranted ) {
        eventBus.post(new PermissionGrantedEvent(permissionGroup.getRequestCode()));
      }
    }
  }

}
