package me.sniggle.android.utils.service.handler;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.LinkedList;
import java.util.Queue;

import me.sniggle.android.utils.application.BaseContext;

/**
 * Created by iulius on 11/03/16.
 */
public class BaseEventHandler<Ctx extends BaseContext> implements EventHandler {

  /**
   * A thread safe handler thread that indicates whether the task could be posted
   * concurrently successfully
   */
  private final class EventHandlerThread extends HandlerThread {

    private Handler handler;
    private Queue<Runnable> bufferQueue = new LinkedList<>();

    public EventHandlerThread(String name) {
      super(name);
    }

    @Override
    protected void onLooperPrepared() {
      super.onLooperPrepared();
      synchronized (this) {
        handler = new Handler(getLooper());
      }
      Runnable task = null;
      while( (task = bufferQueue.poll()) != null ) {
        post(task);
      }
    }

    /**
     * post the task to the thread if available
     *
     * @param runnable
     *  the task to execute
     * @return true if the task could be posted to the concurrent thread
     */
    public boolean post(Runnable runnable) {
      boolean result = false;
      synchronized (this) {
        if( handler != null ) {
          handler.post(runnable);
          result = true;
        } else {
          bufferQueue.add(runnable);
        }
      }
      return result;
    }

  }

  private final Ctx appContext;
  private final EventHandlerThread handlerThread = new EventHandlerThread(getClass().getCanonicalName());

  /**
   * constructor
   *
   * @param appContext
   *    the main app dependency context
   */
  protected BaseEventHandler(Ctx appContext) {
    this.appContext = appContext;
  }

  /**
   *
   * @return the app dependency context
   */
  protected Ctx getAppContext() {
    return appContext;
  }

  /**
   * publishes an event to the applications event bus
   *
   * @param event
   *  the event to be published
   */
  protected void publishEvent(Object event) {
    appContext.getBus().post(event);
  }

  /**
   * performs a task on a background thread
   *
   * @param task
   *  the task to be performed
   * @return true if the task could be posted to the background thread
   */
  protected boolean runBackgroundTask(Runnable task) {
    if( !handlerThread.isAlive() ) {
      handlerThread.start();
    }
    return handlerThread.post(task);
  }

  /**
   * @see EventHandler#onCreate()
   */
  public void onCreate() {
  }

  /**
   * @see EventHandler#onDestroy()
   */
  public void onDestroy() {
    if( handlerThread.isAlive() ) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        handlerThread.quitSafely();
      } else {
        handlerThread.quit();
      }
    }
  }

}
