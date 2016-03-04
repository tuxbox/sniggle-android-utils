package me.sniggle.android.utils.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by iulius on 04/03/16.
 */
public class MainThreadBus extends Bus {

  private static final Object LOCK = new Object();
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final Set<Object> eventCache = new HashSet<>();
  private boolean active = false;

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

  public void activateBus() {
    synchronized (LOCK) {
      for( Object event : eventCache ) {
        post(event);
      }
      eventCache.clear();
      active = true;
    }
  }

  @Override
  public void post(final Object event) {
    synchronized (LOCK) {
      if (active) {
        if (event != null &&
            event instanceof StickyThreadEvent &&
            ((StickyThreadEvent) event).isStickToMainThread()) {
          if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
          } else {
            handler.post(new Runnable() {
              @Override
              public void run() {
                MainThreadBus.super.post(event);
              }
            });
          }
        } else {
          super.post(event);
        }
      } else {
        eventCache.add(event);
      }
    }
  }
}
