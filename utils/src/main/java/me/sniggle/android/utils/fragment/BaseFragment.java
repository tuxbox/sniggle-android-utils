package me.sniggle.android.utils.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.presenter.BasePresenter;

/**
 * A convenience fragment implementation, automatically inflating the associated layout,
 * creating the appropriate presenter, registering the fragment as subscriber to the event bus
 * and allowing to publish new events
 *
 * @author iulius
 * @since 1.0
 */
public abstract class BaseFragment<Ctx extends BaseContext, Application extends BaseApplication<Ctx>, Presenter extends BasePresenter<Ctx>> extends Fragment {

  private final Class<Presenter> presenterClass;
  private final int layoutId;

  private Presenter presenter;

  /**
   *
   * @param layoutId
   *  the id of the required layout
   * @param presenterClass
   *  the class of the presenter to be used
   */
  protected BaseFragment(int layoutId, Class<Presenter> presenterClass) {
    this.layoutId = layoutId;
    this.presenterClass = presenterClass;
  }

  /**
   *
   * @return
   *    the app's dependency context
   */
  protected Ctx getAppContext() {
    return ((Application)getActivity().getApplication()).getAppContext();
  }

  /**
   * publishes an event to the app's event bus
   *
   * @param event
   *  event to be published
   */
  protected void publishEvent(Object event) {
    getAppContext().getBus().post(event);
  }

  protected Presenter createPresenter() {
    Presenter presenter = null;
    try {
      Constructor<Presenter> constructor = presenterClass.getConstructor(getAppContext().getClass());
      presenter = constructor.newInstance(getAppContext());
    } catch (Exception e) {
      Log.e("fragment-presenter", e.getMessage());
    }
    return presenter;
  }

  /**
   * called before view creating to allow early fragment configurations, e.g. registering
   * the fragment to the app's event bus
   */
  protected void preCreateView() {
    getAppContext().getBus().register(this);
  }

  /**
   * creates the view like the Android method {#onCreateView}
   *
   * @param inflater
   *  the inflater
   * @param container
   *  the container
   * @param savedInstanceState
   *  the saved instance state
   * @return the inflated view
   */
  protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(layoutId, container, false);
  }

  /**
   * performs configurations using the newly created view, e.g. initializing the associated presenter
   *
   * @param view
   *  the just created view
   * @param savedInstanceState
   *  the saved instance state
   */
  protected void postCreateView(View view, Bundle savedInstanceState) {
    presenter = createPresenter();
    if( presenter == null ) {
      throw new RuntimeException("Failed to create presenter for fragment");
    }
    presenter.onViewCreated(view);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    preCreateView();
    View result = createView(inflater, container, savedInstanceState);
    postCreateView(result, savedInstanceState);
    return result;
  }

  @Override
  public void onDestroyView() {
    getAppContext().getBus().unregister(this);
    this.presenter.onDestroyView();
    presenter = null;
    super.onDestroyView();
  }

}
