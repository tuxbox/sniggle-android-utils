package me.sniggle.android.utils.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, AdapterItem> extends RecyclerView.Adapter<VH> {

  protected SparseArray<AdapterItem> data;
  protected final int itemViewLayoutId;

  protected BaseRecyclerViewAdapter(int itemViewLayoutId) {
    this.itemViewLayoutId = itemViewLayoutId;
  }

  public void updateAdapterData(SparseArray<AdapterItem> adapterData) {
    this.data = adapterData;
    notifyDataSetChanged();
  }

  protected abstract VH createViewHolder(View itemView);

  protected abstract void bindItemToView(VH holder, AdapterItem item, int position);

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return createViewHolder(LayoutInflater.from(parent.getContext()).inflate(itemViewLayoutId, parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    bindItemToView(holder, data.get(position), position);
  }

  @Override
  public int getItemCount() {
    return data == null ? 0 : data.size();
  }

}
