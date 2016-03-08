package me.sniggle.android.utils.presenter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import me.sniggle.android.utils.adapter.BaseRecyclerViewAdapter;
import me.sniggle.android.utils.application.BaseContext;

/**
 * Base class implementing FragmentPresenter for an RecyclerView
 */
public class BaseFragmentRecyclerPresenter<Adapter extends BaseRecyclerViewAdapter, AdapterItem, Ctx extends BaseContext> extends BaseRecyclerPresenter<Adapter, AdapterItem, Ctx> implements FragmentPresenter{

  protected BaseFragmentRecyclerPresenter(Ctx appContext, int recyclerViewId) {
    super(appContext, recyclerViewId);
  }

  protected BaseFragmentRecyclerPresenter(Ctx appContext, int recyclerViewId, int loadingContainerId) {
    super(appContext, recyclerViewId, loadingContainerId);
  }

  @Override
  public void onAttach(Context context) {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
  }

  @Override
  public void onCreateView() {
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    preViewCreated();
    viewCreated(view);
    postViewCreated();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
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
