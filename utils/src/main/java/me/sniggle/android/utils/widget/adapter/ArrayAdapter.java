package me.sniggle.android.utils.widget.adapter;

import java.util.Arrays;

import me.sniggle.android.utils.application.BaseContext;

/**
 * A convenience adapter implementation that takes out all the boiler plate code.
 * It lets you focus on the important stuff, resetting your convert view and binding values
 * to the adapter item view
 *
 * @author iulius
 * @since 1.3
 */
public abstract class ArrayAdapter<T, Ctx extends BaseContext> extends ListAdapter<T, Ctx> {

  protected ArrayAdapter(Ctx context, int layoutId) {
    this(context, layoutId, null);
  }

  protected ArrayAdapter(Ctx context, int layoutId, T[] values) {
    super(context, layoutId, Arrays.asList(values));
  }

  public void updateAdapterData(T[] values) {
    super.updateAdapterData(Arrays.asList(values));
  }

}
