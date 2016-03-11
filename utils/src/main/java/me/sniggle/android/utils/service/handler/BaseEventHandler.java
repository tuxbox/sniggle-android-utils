package me.sniggle.android.utils.service.handler;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import me.sniggle.android.utils.application.BaseContext;

/**
 * Created by iulius on 11/03/16.
 */
public class BaseEventHandler<Ctx extends BaseContext> implements EventHandler {

  private final Ctx appContext;
  private final HandlerThread handlerThread = new HandlerThread(getClass().getCanonicalName());

  protected BaseEventHandler(Ctx appContext) {
    this.appContext = appContext;
  }

  protected Ctx getAppContext() {
    return appContext;
  }

  protected void publishEvent(Object event) {
    appContext.getBus().post(event);
  }

  protected void runBackgroundTask(Runnable task) {
    new Handler(handlerThread.getLooper()).post(task);
  }

  public void onCreate() {
    handlerThread.start();
  }

  public void onDestroy() {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      handlerThread.quitSafely();
    } else {
      handlerThread.quit();
    }
  }

}
