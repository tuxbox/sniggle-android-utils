package me.sniggle.android.utils.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Convenience recycler adapter providing an easy API to populate the recycler view
 *
 * @param <VH>
 *    the view holder
 * @param <AdapterItem>
 *    the adapter item
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, AdapterItem> extends RecyclerView.Adapter<VH> {

  protected SparseArray<AdapterItem> data;
  protected final int itemViewLayoutId;

  /**
   *
   * @param itemViewLayoutId
   *    the item layout
   */
  protected BaseRecyclerViewAdapter(int itemViewLayoutId) {
    this.itemViewLayoutId = itemViewLayoutId;
  }

  /**
   * updates the recycler view adapter
   *
   * @param adapterData
   *  the adapter data
   */
  public void updateAdapterData(SparseArray<AdapterItem> adapterData) {
    this.data = adapterData;
    notifyDataSetChanged();
  }

  /**
   * creates the view holder for the itemView
   *
   * @param itemView
   *  the item view
   * @return the view holder for the item view
   */
  protected abstract VH createViewHolder(View itemView);

  /**
   * binds an adapter item to the view holder
   *
   * @param holder
   *  the view holder
   * @param item
   *  the item
   * @param position
   *  the position of the item
   */
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
