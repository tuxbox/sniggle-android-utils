package me.sniggle.android.utils.application;

import android.app.Application;

import org.acra.ACRA;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class BaseApplication<Context extends BaseContext> extends Application {

  private Context appContext;

  public BaseApplication() {
  }

  protected abstract Context createContext();

  public Context getAppContext() {
    return appContext;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    ACRA.init(this);
    appContext = createContext();
    appContext.onCreate();
  }

  @Override
  public void onTerminate() {
    appContext.onTerminate();
    appContext = null;
    super.onTerminate();
  }
}
