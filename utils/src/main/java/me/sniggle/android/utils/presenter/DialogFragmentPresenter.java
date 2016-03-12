package me.sniggle.android.utils.presenter;

import android.content.DialogInterface;

/**
 * The required minimum interface for a Dialog Fragment Presenter
 */
public interface DialogFragmentPresenter extends FragmentPresenter {

  /**
   *
   * @return the click listener for dialog confirmation
   */
  DialogInterface.OnClickListener getPositiveClickListener();

  /**
   *
   * @return the click listener for dialog dismissal
   */
  DialogInterface.OnClickListener getNegativeClickListener();

}
