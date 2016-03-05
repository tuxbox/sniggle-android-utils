package me.sniggle.android.utils.widget.adapter;

import java.util.List;

import me.sniggle.android.utils.application.BaseContext;

/**
 * A convenience adapter implementation that takes out all the boiler plate code.
 * It lets you focus on the important stuff, resetting your convert view and binding values
 * to the adapter item view
 *
 * @author iulius
 * @since 1.2
 */
public abstract class EnumAdapter<T extends Enum<T>, Ctx extends BaseContext> extends ListAdapter<T, Ctx> {

  protected EnumAdapter(Ctx context, int layoutId, List<T> values) {
    super(context, layoutId, values);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).ordinal();
  }

}
