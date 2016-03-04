package me.sniggle.android.utils.fragment;

import android.content.Context;
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
 * Created by iulius on 04/03/16.
 */
public abstract class BaseFragment<Ctx extends BaseContext, Application extends BaseApplication<Ctx>, Presenter extends BasePresenter<Ctx>> extends Fragment {

  private final Class<Presenter> presenterClass;
  private final int layoutId;

  private Presenter presenter;

  protected BaseFragment(int layoutId, Class<Presenter> presenterClass) {
    this.layoutId = layoutId;
    this.presenterClass = presenterClass;
  }

  protected Ctx getAppContext() {
    return ((Application)getActivity().getApplication()).getAppContext();
  }

  protected void publishEvent(Object event) {
    getAppContext().getBus().post(event);
  }

  protected void preCreateView() {
    getAppContext().getBus().register(this);
  }

  protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(layoutId, container, false);
  }

  protected void postCreateView(View view, Bundle savedInstanceState) {
    try {
      Constructor<Presenter> constructor = presenterClass.getConstructor(Context.class, getAppContext().getClass());
      presenter = constructor.newInstance(getActivity(), getAppContext());
      presenter.onViewCreated(view);
    } catch (Exception e) {
      Log.e("fragment-presenter", e.getMessage());
    }
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
