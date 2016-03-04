package me.sniggle.android.utils.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.ThreadEnforcer;

/**
 * Created by iulius on 04/03/16.
 */
public class MainThreadBus extends ActivatorBus {

  private static final Object LOCK = new Object();
  private final Handler handler = new Handler(Looper.getMainLooper());

  public MainThreadBus() {
  }

  public MainThreadBus(ThreadEnforcer enforcer) {
    super(enforcer);
  }

  public MainThreadBus(ThreadEnforcer enforcer, String identifier) {
    super(enforcer, identifier);
  }

  public MainThreadBus(String identifier) {
    super(identifier);
  }

  protected void postEvent(final Object event) {
    if (event != null &&
        event instanceof StickyThreadEvent &&
        ((StickyThreadEvent) event).isStickToMainThread()) {
      if (Looper.myLooper() == Looper.getMainLooper()) {
        super.postEvent(event);
      } else {
        handler.post(new Runnable() {
          @Override
          public void run() {
            MainThreadBus.super.postEvent(event);
          }
        });
      }
    } else {
      super.postEvent(event);
    }
  }

}
