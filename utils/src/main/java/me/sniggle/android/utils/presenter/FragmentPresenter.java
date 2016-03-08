package me.sniggle.android.utils.presenter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * This interface binds a presenter to the fragment lifecycle.
 * Will be automatically called if the Fragment derives from BaseFragment
 */
public interface FragmentPresenter {

  /**
   * binds to Fragment#onAttach(Context)
   *
   * @param context
   *  the context attached to the Fragment
   */
  void onAttach(Context context);

  /**
   * binds to Fragment#onCreate(Bundle)
   *
   * @param savedInstanceState
   *  the fragments last known state
   */
  void onCreate(Bundle savedInstanceState);

  /**
   * binds to Fragment#onCreateView()
   */
  void onCreateView();

  /**
   * binds to Fragment#onViewCreated(View, Bundle)
   *
   * @param view
   *  the view created
   * @param savedInstanceState
   *   the last known state
   */
  void onViewCreated(View view, Bundle savedInstanceState);

  /**
   * binds to Fragment#onActivityCreated(Activity)
   *
   * @param savedInstanceState
   *  the last known state
   */
  void onActivityCreated(Bundle savedInstanceState);

  /**
   * binds to Fragment#onStart()
   */
  void onStart();

  /**
   * binds to Fragment#onResume()
   */
  void onResume();

  /**
   * binds to Fragment#onPause()
   */
  void onPause();

  /**
   * binds to Fragment#onStop()
   */
  void onStop();

  /**
   * binds to Fragment#onDestroyView()
   */
  void onDestroyView();

  /**
   * binds to Fragment#onDestroy()
   */
  void onDestroy();

  /**
   * binds to Fragment#onDetach()
   */
  void onDetach();

}
