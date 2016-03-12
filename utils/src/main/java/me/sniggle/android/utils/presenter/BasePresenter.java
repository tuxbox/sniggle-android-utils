package me.sniggle.android.utils.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import butterknife.ButterKnife;
import me.sniggle.android.utils.application.BaseContext;

/**
 * Base presenter to follow the Model-View-Controller-Presenter pattern.
 * Offers convenience for derived presenters as it e.g. automatically handles
 * the state of presenter associated views using ButterKnife
 *
 * @author iulius
 * @since 1.0
 */
public abstract class BasePresenter<Ctx extends BaseContext> {

  private final Ctx appContext;

  protected BasePresenter(Ctx appContext) {
    this.appContext = appContext;
  }

  protected Ctx getAppContext() {
    return appContext;
  }

  /**
   * publishes an event to the applications event bus
   *
   * @param event
   *  the event to publish
   */
  protected void publishEvent(Object event) {
    appContext.getBus().post(event);
  }

  /**
   * method bound to #onViewCreated used to configure stuff before e.g. the views are bound
   */
  protected void preViewCreated() {

  }

  /**
   * method bound to #onViewCreated used to actually work with the parent view and e.g. bind
   * the children views to this presenter
   *
   * @param activity
   *    the source activity
   */
  protected void viewCreated(Activity activity) {
    ButterKnife.bind(this, activity);
  }

  /**
   * method bound to #onViewCreated used to actually work with the parent view and e.g. bind
   * the children views to this presenter
   *
   * @param view
   *    the source view
   */
  protected void viewCreated(View view) {
    ButterKnife.bind(this, view);
  }

  /**
   * method bound to #onViewCreated used to actually work with the parent view and e.g. bind
   * the children views to this presenter
   *
   * @param dialog
   *    the source dialog
   */
  protected void viewCreated(Dialog dialog) {
    ButterKnife.bind(this, dialog);
  }

  /**
   * method bound to #onViewCreated used to configure e.g. bound views etc.
   */
  protected void postViewCreated() {

  }

  /**
   * life cycle method used to initialize and e.g. bind views
   * of the given activity
   *
   * @param activity
   *  the parent activity
   */
  public void onViewCreated(Activity activity) {
    preViewCreated();
    viewCreated(activity);
    postViewCreated();
  }

  /**
   * life cycle method used to initialize and e.g. bind views
   * of the given container
   *
   * @param container
   */
  public void onViewCreated(View container) {
    preViewCreated();
    viewCreated(container);
    postViewCreated();
  }

  /**
   * life cycle method used to clean up, e.g. free the bound views
   */
  public void onDestroyView() {
    ButterKnife.unbind(this);
  }

}
