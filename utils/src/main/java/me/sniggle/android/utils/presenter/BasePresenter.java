package me.sniggle.android.utils.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;
import me.sniggle.android.utils.application.BaseContext;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class BasePresenter<Ctx extends BaseContext> {

  private final Context context;
  private final Ctx appContext;

  protected BasePresenter(Context context, Ctx appContext) {
    this.context = context;
    this.appContext = appContext;
  }

  protected void init() {

  }

  protected Context getContext() {
    return context;
  }

  protected Ctx getAppContext() {
    return appContext;
  }

  protected void publishEvent(Object event) {
    appContext.getBus().post(event);
  }

  public void onViewCreated(Activity activity) {
    init();
    ButterKnife.bind(this, activity);
  }

  public void onViewCreated(View view) {
    init();
    ButterKnife.bind(this, view);
  }

  public void onDestroyView() {
    ButterKnife.unbind(this);
  }

}
