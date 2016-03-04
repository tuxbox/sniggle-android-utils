package me.sniggle.android.utils.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import butterknife.ButterKnife;
import me.sniggle.android.utils.adapter.BaseRecyclerViewAdapter;
import me.sniggle.android.utils.application.BaseContext;

/**
 * Created by iulius on 04/03/16.
 */
public class BaseRecyclerPresenter<Adapter extends BaseRecyclerViewAdapter, AdapterItem, Ctx extends BaseContext> extends BasePresenter<Ctx> {

  protected RecyclerView recyclerView;
  protected View loadingContainer;

  protected final int recyclerViewId;
  protected final int loadingContainerId;
  private Adapter adapter;

  protected BaseRecyclerPresenter(Context context, Ctx appContext, int recyclerViewId) {
    this(context, appContext, recyclerViewId, -1);
  }

  protected BaseRecyclerPresenter(Context context, Ctx appContext, int recyclerViewId, int loadingContainerId) {
    super(context, appContext);
    this.recyclerViewId = recyclerViewId;
    this.loadingContainerId = loadingContainerId;
  }

  public void updateAdapterData(SparseArray<AdapterItem> adapterData) {
    adapter.updateAdapterData(adapterData);
    loadingContainer.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  protected void configureRecyclerView(RecyclerView recyclerView) {
    //recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.divider));
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onViewCreated(Activity activity) {
    super.onViewCreated(activity);
    if( recyclerViewId > 0 ) {
      recyclerView = ButterKnife.findById(activity, recyclerViewId);
    }
    if( loadingContainerId > 0 ) {
      loadingContainer = ButterKnife.findById(activity, loadingContainerId);
    }
    configureRecyclerView(recyclerView);
  }

  @Override
  public void onViewCreated(View view) {
    super.onViewCreated(view);
    if( recyclerViewId > 0 ) {
      recyclerView = ButterKnife.findById(view, recyclerViewId);
    }
    if( loadingContainerId > 0 ) {
      loadingContainer = ButterKnife.findById(view, loadingContainerId);
    }
    configureRecyclerView(recyclerView);
  }
}
