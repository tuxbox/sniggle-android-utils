package me.sniggle.android.utils.presenter;

import android.os.Bundle;

import me.sniggle.android.utils.application.BaseContext;

/**
 * Base class implementing ActivityPresenter
 */
public class BaseActivityPresenter<Ctx extends BaseContext> extends BasePresenter<Ctx> implements ActivityPresenter {

  protected BaseActivityPresenter(Ctx appContext) {
    super(appContext);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

  }

  @Override
  public void onStart() {

  }

  @Override
  public void onResume() {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void onStop() {

  }

  @Override
  public void onDestroy() {
    onDestroyView();
  }
}
