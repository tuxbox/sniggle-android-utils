package me.sniggle.android.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Constructor;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.application.BaseCouchbase;
import me.sniggle.android.utils.otto.ActivatorBus;
import me.sniggle.android.utils.presenter.ActivityPresenter;
import me.sniggle.android.utils.presenter.BaseNavigationActivityPresenter;

/**
 * Created by iulius on 15/03/16.
 */
public abstract class BaseNavigationActivity<
    HttpService,
    Database extends BaseCouchbase,
    AppBus extends ActivatorBus,
    Ctx extends BaseContext<HttpService, Database, AppBus>,
    Application extends BaseApplication<Ctx>,
    Presenter extends BaseNavigationActivityPresenter<Ctx> & ActivityPresenter
    > extends BaseActivity<HttpService, Database, AppBus, Ctx, Application, Presenter>
  implements BaseNavigationActivityPresenter.NavigationCallback{

  /**
   * @param layoutId       the layout id for the activities' layout
   * @param presenterClass
   */
  protected BaseNavigationActivity(int layoutId, Class<Presenter> presenterClass) {
    super(layoutId, presenterClass);
  }

  @Override
  protected Presenter createPresenter() {
    Presenter presenter = null;
    try {
      Constructor<Presenter> constructor = presenterClass.getConstructor(getAppContext().getClass(), BaseNavigationActivityPresenter.NavigationCallback.class);
      presenter = constructor.newInstance(getAppContext(), this);
    } catch (Exception e) {
      Log.e("activity-presenter", e.getMessage());
    }
    return presenter;
  }

  @Override
  public void handleNavigation(Class<? extends Activity> aClass) {
    startActivity(new Intent(this, aClass));
  }

}
