package me.sniggle.android.utils.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.presenter.BasePresenter;
import me.sniggle.android.utils.presenter.DialogFragmentPresenter;

/**
 * Created by iulius on 12/03/16.
 */
public abstract class BaseDialogFragment<Ctx extends BaseContext, Application extends BaseApplication<Ctx>, Presenter extends BasePresenter<Ctx> & DialogFragmentPresenter> extends DialogFragment {

  private final Class<Presenter> presenterClass;

  private Presenter presenter;

  /**
   *
   * @param presenterClass
   *  the class of the presenter to be used
   */
  protected BaseDialogFragment(Class<Presenter> presenterClass) {
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
   *
   * @return
   *  the dialog fragment's presenter
   */
  protected Presenter getPresenter() {
    return presenter;
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

  /**
   * creates the presenter as defined by Presenter.class with constructor taking Ctx as argument
   *
   * @return the presenter or null if instantiation fails
   */
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

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    presenter = createPresenter();
    presenter.onAttach(context);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter.onCreate(savedInstanceState);
  }

  /**
   * creates and sets up the Dialog to be shown
   *
   * @param savedInstanceState
   *  the saved instance state as provided in onCreateDialog
   * @return the dialog to show
   */
  protected abstract Dialog createDialog(Bundle savedInstanceState);

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    presenter.onCreateView();
    preCreateView();
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.onViewCreated(view);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return createDialog(savedInstanceState);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    presenter.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.onResume();
  }

  @Override
  public void onPause() {
    presenter.onPause();
    super.onPause();
  }

  @Override
  public void onStop() {
    presenter.onStop();
    super.onStop();
  }

  @Override
  public void onDestroyView() {
    getAppContext().getBus().unregister(this);
    this.presenter.onDestroyView();
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override
  public void onDetach() {
    presenter.onDetach();
    presenter = null;
    super.onDetach();
  }
}
