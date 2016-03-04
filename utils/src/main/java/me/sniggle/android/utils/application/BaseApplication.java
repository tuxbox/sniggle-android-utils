package me.sniggle.android.utils.application;

import android.app.Application;

import org.acra.ACRA;

/**
 * convenience application object that performs initialization of the dependency context
 * and sets up ACRA for crash reporting
 *
 * @author iulius
 * @since 1.0
 *
 * @param <Context>
 *   the app's dependency context
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
