package me.sniggle.android.utils.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Constructor;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.application.BaseCouchbase;
import me.sniggle.android.utils.otto.ActivatorBus;
import me.sniggle.android.utils.presenter.BasePresenter;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class BaseActivity<
      HttpService,
      Database extends BaseCouchbase,
      AppBus extends ActivatorBus,
      Ctx extends BaseContext<HttpService, Database, AppBus>,
      Application extends BaseApplication<Ctx>,
      Presenter extends BasePresenter<Ctx>
    > extends AppCompatActivity {

  private final int layoutId;
  private final Class<Presenter> presenterClass;
  private Presenter presenter;

  protected BaseActivity(int layoutId, Class<Presenter> presenterClass) {
    super();
    this.layoutId = layoutId;
    this.presenterClass = presenterClass;
  }

  protected Presenter createPresenter() {
    Presenter presenter = null;
    try {
      Constructor<Presenter> constructor = presenterClass.getConstructor(getAppContext().getClass());
      presenter = constructor.newInstance(this);
    } catch (Exception e) {
      Log.e("activity-presenter", e.getMessage());
    }
    return presenter;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layoutId);
    presenter = createPresenter();
    if( presenter == null ) {
      throw new RuntimeException("Failed to create presenter for activity");
    }
    presenter.onViewCreated(this);
    getAppContext().getBus().register(this);
  }

  @Override
  protected void onDestroy() {
    getAppContext().getBus().unregister(this);
    presenter.onDestroyView();
    super.onDestroy();
  }
}
