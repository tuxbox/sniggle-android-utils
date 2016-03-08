package me.sniggle.android.utils.presenter;

import android.os.Bundle;

/**
 * This interface binds a presenter to the activity lifecycle.
 * Will be automatically called if the Activity derives from BaseActivity
 */
public interface ActivityPresenter {

  /**
   * binds to Activity#onCreate(Bundle)
   *
   * @param savedInstanceState
   *    the last know instance state
   */
  void onCreate(Bundle savedInstanceState);

  /**
   * binds to Activity#onStart()
   */
  void onStart();

  /**
   * binds to Activity#onResume()
   */
  void onResume();

  /**
   * binds to Activity#onPause()
   */
  void onPause();

  /**
   * binds to Activity#onStop()
   */
  void onStop();

  /**
   * binds to Activity#onDestroy()
   */
  void onDestroy();

}
