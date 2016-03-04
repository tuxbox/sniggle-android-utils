package me.sniggle.android.utils.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.ThreadEnforcer;

/**
 * Extension of {#ActivatorBus} ensuring that sticky events are executed on the Main Thread if required
 *
 * @author iulius
 * @since 1.0
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
