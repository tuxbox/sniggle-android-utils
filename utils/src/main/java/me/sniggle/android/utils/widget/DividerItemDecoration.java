package me.sniggle.android.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by iulius on 04/03/16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

  private final Context context;
  private final Drawable divider;

  public DividerItemDecoration(Context context) {
    this.context = context;
    final TypedArray styledAttributes = context.obtainStyledAttributes(new int[]{android.R.attr.divider});
    divider = styledAttributes.getDrawable(0);
    styledAttributes.recycle();
  }

  public DividerItemDecoration(Context context, int resourceId) {
    this.context = context;
    this.divider = ContextCompat.getDrawable(context, resourceId);
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + this.divider.getIntrinsicHeight();

      this.divider.setBounds(left, top, right, bottom);

      this.divider.draw(c);
    }
  }

}
