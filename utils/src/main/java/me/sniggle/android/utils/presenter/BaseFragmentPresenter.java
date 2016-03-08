package me.sniggle.android.utils.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.sniggle.android.utils.application.BaseContext;

/**
 * Base class implementing FragmentPresenter
 */
public abstract class BaseFragmentPresenter<Ctx extends BaseContext> extends BasePresenter<Ctx> implements FragmentPresenter {

  protected BaseFragmentPresenter(Ctx appContext) {
    super(appContext);
  }

  @Override
  public void onAttach(Context context) {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
  }

  @Override
  public void onCreateView() {
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    preViewCreated();
    viewCreated(view);
    postViewCreated();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
  }

  @Override
  public void onDetach() {
  }

}
