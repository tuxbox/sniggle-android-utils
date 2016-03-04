package me.sniggle.android.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;

import java.util.LinkedList;

import me.sniggle.android.utils.application.BaseApplication;
import me.sniggle.android.utils.application.BaseContext;
import me.sniggle.android.utils.application.BaseCouchbase;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class BaseEventService<HttpService, Database extends BaseCouchbase, Ctx extends BaseContext<HttpService, Database>, App extends BaseApplication<Ctx>> extends Service {

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

  protected abstract void activateBus(Bus bus);

  protected abstract void deactivateBus(Bus bus);

  protected abstract void init();

  protected void unregisterHandlers() {
    Object eventHandler = null;
    while( ( eventHandler = eventHandlers.pollLast() ) != null ) {
      getAppContext().getBus().unregister(eventHandler);
    }
  }

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
    init();
    activateBus(getAppContext().getBus());
  }

  @Override
  public void onDestroy() {
    deactivateBus(getAppContext().getBus());
    unregisterHandlers();
    super.onDestroy();
  }
}
