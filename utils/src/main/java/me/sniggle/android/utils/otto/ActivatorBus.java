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
public class ActivatorBus extends Bus {

  private static final Object LOCK = new Object();
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final Set<Object> eventCache = new HashSet<>();
  private boolean active = false;

  public ActivatorBus() {
  }

  public ActivatorBus(ThreadEnforcer enforcer) {
    super(enforcer);
  }

  public ActivatorBus(ThreadEnforcer enforcer, String identifier) {
    super(enforcer, identifier);
  }

  public ActivatorBus(String identifier) {
    super(identifier);
  }

  protected void postEvent(Object event) {
    super.post(event);
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

  public void deactivateBus() {
    synchronized (LOCK) {
      active = false;
    }
  }

  @Override
  public void post(final Object event) {
    synchronized (LOCK) {
      if (active) {
        postEvent(event);
      } else {
        eventCache.add(event);
      }
    }
  }

}
