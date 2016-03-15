package me.sniggle.android.utils.presenter;

import android.app.Activity;
import android.support.design.widget.NavigationView;

import me.sniggle.android.utils.application.BaseContext;

/**
 * Created by iulius on 15/03/16.
 */
public abstract class BaseNavigationActivityPresenter<Ctx extends BaseContext> extends BaseActivityPresenter<Ctx> implements NavigationView.OnNavigationItemSelectedListener {

  public interface NavigationCallback {

    void handleNavigation(Class<? extends Activity> activityClass);

  }

  protected final NavigationCallback navigationCallback;

  protected BaseNavigationActivityPresenter(Ctx appContext, NavigationCallback navigationCallback) {
    super(appContext);
    this.navigationCallback = navigationCallback;
  }

}
