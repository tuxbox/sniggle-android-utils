package me.sniggle.android.utils.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import butterknife.ButterKnife;
import me.sniggle.android.utils.adapter.BaseRecyclerViewAdapter;
import me.sniggle.android.utils.application.BaseContext;

/**
 * Specialized presenter to unify the behaviour for recycler views
 * and updates of recycler views.
 *
 * @author iulius
 * @since 1.0
 */
public class BaseRecyclerPresenter<Adapter extends BaseRecyclerViewAdapter, AdapterItem, Ctx extends BaseContext> extends BasePresenter<Ctx> {

  protected RecyclerView recyclerView;
  protected View loadingContainer;

  protected final int recyclerViewId;
  protected final int loadingContainerId;
  private Adapter adapter;

  /**
   *
   * @param appContext
   *  the applications dependency context
   * @param recyclerViewId
   *  the id of the recycler view
   */
  protected BaseRecyclerPresenter(Ctx appContext, int recyclerViewId) {
    this(appContext, recyclerViewId, -1);
  }

  /**
   *
   * @param appContext
   *   the applications dependency context
   * @param recyclerViewId
   *   the id of the recycler view
   * @param loadingContainerId
   *   the id of the loading container to be shown instead of the recycler view while loading data
   */
  protected BaseRecyclerPresenter(Ctx appContext, int recyclerViewId, int loadingContainerId) {
    super(appContext);
    this.recyclerViewId = recyclerViewId;
    this.loadingContainerId = loadingContainerId;
  }

  /**
   * updates the recycler views adapter data
   *
   * @param adapterData
   *  the data to be shown
   */
  public void updateAdapterData(SparseArray<AdapterItem> adapterData) {
    adapter.updateAdapterData(adapterData);
    loadingContainer.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  /**
   * configures the recycler view, e.g. layout, adapter etc.
   *
   * defaults to a list layout
   *
   * @param recyclerView
   *  the recycler view bound to this presenter
   */
  protected void configureRecyclerView(RecyclerView recyclerView) {
    //recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.divider));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getAppContext().getContext()));
    recyclerView.setAdapter(adapter);
  }

  @Override
  protected void viewCreated(Activity activity) {
    super.viewCreated(activity);
    if( recyclerViewId > 0 ) {
      recyclerView = ButterKnife.findById(activity, recyclerViewId);
    }
    if( loadingContainerId > 0 ) {
      loadingContainer = ButterKnife.findById(activity, loadingContainerId);
    }
  }

  @Override
  protected void viewCreated(Dialog dialog) {
    super.viewCreated(dialog);
    if( recyclerViewId > 0 ) {
      recyclerView = ButterKnife.findById(dialog, recyclerViewId);
    }
    if( loadingContainerId > 0 ) {
      loadingContainer = ButterKnife.findById(dialog, loadingContainerId);
    }
  }

  @Override
  protected void viewCreated(View view) {
    super.viewCreated(view);
    if( recyclerViewId > 0 ) {
      recyclerView = ButterKnife.findById(view, recyclerViewId);
    }
    if( loadingContainerId > 0 ) {
      loadingContainer = ButterKnife.findById(view, loadingContainerId);
    }
  }

  @Override
  protected void postViewCreated() {
    super.postViewCreated();
    if( recyclerViewId > 0 ) {
      configureRecyclerView(recyclerView);
    }
  }

}
