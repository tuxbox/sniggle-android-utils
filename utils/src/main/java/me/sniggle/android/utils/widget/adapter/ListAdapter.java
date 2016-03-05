package me.sniggle.android.utils.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import me.sniggle.android.utils.application.BaseContext;

/**
 * A convenience adapter implementation that takes out all the boiler plate code.
 * It lets you focus on the important stuff, resetting your convert view and binding values
 * to the adapter item view
 *
 * @author iulius
 * @since 1.3
 */
public abstract class ListAdapter<T, Ctx extends BaseContext> extends BaseAdapter {

  protected final Ctx context;
  protected final int layoutId;
  protected final LayoutInflater layoutInflater;
  protected List<T> values;

  protected ListAdapter(Ctx context, int layoutId) {
    this(context, layoutId, null);
  }

  protected ListAdapter(Ctx context, int layoutId, List<T> values) {
    this.context = context;
    this.layoutId = layoutId;
    this.values = values;
    this.layoutInflater = LayoutInflater.from(context.getContext());
  }

  public void updateAdapterData(List<T> values) {
    this.values = values;
    notifyDataSetChanged();
  }
  @Override
  public int getCount() {
    return values == null ? 0 : values.size();
  }

  @Override
  public T getItem(int position) {
    return values.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View result = convertView;
    if( result == null ) {
      result = layoutInflater.inflate(layoutId, null);
    } else {
      resetView(result);
    }
    bindView(position, result, getItem(position));
    return result;
  }

  /**
   * bind appropriate data to the item view
   *
   * @param position
   *  the item position
   * @param itemView
   *  the item view
   * @param item
   *  the item to be bound
   */
  protected abstract void bindView(int position, View itemView, T item);

  /**
   * reset the item view in order to avoid stale values in your view
   *
   * @param itemView
   *    the item view to reset
   */
  protected abstract void resetView(View itemView);

}
