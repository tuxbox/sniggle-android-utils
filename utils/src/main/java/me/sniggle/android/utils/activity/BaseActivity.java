package me.sniggle.android.utils.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Constructor;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.application.BaseCouchbase;
import me.sniggle.android.utils.otto.ActivatorBus;
import me.sniggle.android.utils.permission.BasePermissionDelegate;
import me.sniggle.android.utils.permission.PermissionDelegate;
import me.sniggle.android.utils.presenter.ActivityPresenter;
import me.sniggle.android.utils.presenter.BaseActivityPresenter;
import me.sniggle.android.utils.presenter.BasePresenter;

/**
 * Convenience BaseActivity that initializes the activity with all necessary
 * dependencies
 *
 * @param <HttpService>
 *   the Retrofit HTTP service
 * @param <Database>
 *   the Couchbase Database
 * @param <AppBus>
 *   the app's event bus
 * @param <Ctx>
 *   the app's dependency context
 * @param <Application>
 *   the app's application Object
 * @param <Presenter>
 *   the presenter for this activity
 */
public abstract class BaseActivity<
      HttpService,
      Database extends BaseCouchbase,
      AppBus extends ActivatorBus,
      Ctx extends BaseContext<HttpService, Database, AppBus>,
      Application extends BaseApplication<Ctx>,
      Presenter extends BasePresenter<Ctx> & ActivityPresenter
    > extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

  private final int layoutId;
  protected final Class<Presenter> presenterClass;
  private Presenter presenter;
  private PermissionDelegate permissionDelegate;

  /**
   *
   * @param layoutId
   *  the layout id for the activities' layout
   * @param presenterClass
   *  the class of the associated presenter
   */
  protected BaseActivity(int layoutId, Class<Presenter> presenterClass) {
    super();
    this.layoutId = layoutId;
    this.presenterClass = presenterClass;
  }

  /**
   * default presenter factory method
   *
   * @return the appropriate presenter
   */
  protected Presenter createPresenter() {
    Presenter presenter = null;
    try {
      Constructor<Presenter> constructor = presenterClass.getConstructor(getAppContext().getClass());
      presenter = constructor.newInstance(getAppContext());
    } catch (Exception e) {
      Log.e("activity-presenter", e.getMessage());
    }
    return presenter;
  }

  protected <T extends BasePermissionDelegate<?>> T createBasePermissionDelegate() {
    return  null;
  }

  protected Ctx getAppContext() {
    return ((Application)getApplication()).getAppContext();
  }

  protected Presenter getPresenter() {
    return presenter;
  }

  protected void publishEvent(Object event) {
    getAppContext().getBus().post(event);
  }

  protected void preCreate() {
    permissionDelegate = createBasePermissionDelegate();
  }

  protected void create(Bundle savedInstanceState) {
    setContentView(layoutId);
    presenter = createPresenter();
    if( presenter == null ) {
      throw new RuntimeException("Failed to create presenter for activity");
    }
    presenter.onCreate(savedInstanceState);
  }

  protected void postCreate(Bundle savedInstanceState) {
    presenter.onViewCreated(this);
    getAppContext().getBus().register(this);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if( permissionDelegate != null ) {
      permissionDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    preCreate();
    create(savedInstanceState);
    postCreate(savedInstanceState);
    if( permissionDelegate != null ) {
      permissionDelegate.ensurePermissions();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    presenter.onResume();
  }

  @Override
  protected void onPause() {
    presenter.onPause();
    super.onPause();
  }

  @Override
  protected void onStop() {
    presenter.onStop();
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    permissionDelegate = null;
    presenter.onDestroy();
    getAppContext().getBus().unregister(this);
    super.onDestroy();
  }
}
