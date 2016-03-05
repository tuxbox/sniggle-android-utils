package me.sniggle.android.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.LinkedList;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.application.BaseCouchbase;
import me.sniggle.android.utils.otto.ActivatorBus;

/**
 * Basic Android Service supposed to be used for all events that trigger long-running tasks,
 * like database access, API querying etc.
 *
 * @author iulius
 * @since 1.0
 */
public abstract class BaseEventService<HttpService, Database extends BaseCouchbase, AppBus extends ActivatorBus, Ctx extends BaseContext<HttpService, Database, AppBus>, App extends BaseApplication<Ctx>> extends Service {

  private final LinkedList<Object> eventHandlers = new LinkedList<>();

  protected Ctx getAppContext() {
    return ((App)getApplication()).getAppContext();
  }

  protected Database getDatabase() {
    return getAppContext().getDatabase();
  }

  protected HttpService getHttpService() {
    return getAppContext().getHttpService();
  }

  protected void publishEvent(Object event) {
    getAppContext().getBus().post(event);
  }

  /**
   * activates the bus to handle events properly
   *
   * this is necessary as the Service gets started asynchronously by Android
   * hence it might not be available to handle events when the application
   * already fires them
   *
   * @param bus
   *   the applications event bus
   */
  protected void activateBus(AppBus bus) {
    bus.activateBus();
  }

  /**
   * deactivates the bus in order to notify that event handlers are not available anymore
   *
   * @see #activateBus(ActivatorBus)
   *
   * @param bus
   *  the applications event bus
   */
  protected void deactivateBus(AppBus bus) {
    bus.deactivateBus();
  }

  /**
   * initializer method, e.g. you can add your event handlers here
   * bound to #onCreate
   */
  protected abstract void preCreate();

  /**
   * initializer method, set up your service here, bound to #onCreate
   */
  protected void create() {
  }

  /**
   * initializer method, configure your service here and e.g. activate the bus, bound to #onCreate
   */
  protected void postCreate() {
    activateBus(getAppContext().getBus());
  }

  /**
   * life cycle function, deregisters the event handlers properly
   */
  protected void unregisterHandlers() {
    Object eventHandler = null;
    while( ( eventHandler = eventHandlers.pollLast() ) != null ) {
      getAppContext().getBus().unregister(eventHandler);
    }
  }

  /**
   * adds an event handler to the app's event bus
   *
   * @param eventHandler
   *  the event handler to add
   */
  protected void addEventHandler(Object eventHandler) {
    if( eventHandler != null ) {
      getAppContext().getBus().register(eventHandler);
      eventHandlers.add(eventHandler);
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    preCreate();
    create();
    postCreate();
  }

  @Override
  public void onDestroy() {
    deactivateBus(getAppContext().getBus());
    unregisterHandlers();
    super.onDestroy();
  }
}
