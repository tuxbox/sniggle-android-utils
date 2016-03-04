package me.sniggle.android.utils.presenter;

import android.app.Activity;
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

  protected void init() {

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
   * life cycle method used to initialize and e.g. bind views
   * of the given activity
   *
   * @param activity
   *  the parent activity
   */
  public void onViewCreated(Activity activity) {
    init();
    ButterKnife.bind(this, activity);
  }

  /**
   * life cycle method used to initialize and e.g. bind views
   * of the given container
   *
   * @param container
   */
  public void onViewCreated(View container) {
    init();
    ButterKnife.bind(this, container);
  }

  /**
   * life cycle method used to clean up, e.g. free the bound views
   */
  public void onDestroyView() {
    ButterKnife.unbind(this);
  }

}
