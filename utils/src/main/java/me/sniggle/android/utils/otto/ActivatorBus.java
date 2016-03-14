package me.sniggle.android.utils.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A convenience extension of a regular Otto bus that caches events until the Bus is properly setup
 * and all event handlers are ready to handle them.
 * Dispatches all cached events in chronological order upon activation.
 *
 * @author iulius
 * @since 1.0
 */
public class ActivatorBus extends Bus {

  private static final Object LOCK = new Object();
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final Set<Object> eventCache = new LinkedHashSet<>();
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

  /**
   * activates event dispatching
   */
  public void activateBus() {
    synchronized (LOCK) {
      LinkedHashSet<Object> localCopy = new LinkedHashSet<>(eventCache);
      for( Object event : localCopy ) {
        post(event, true);
      }
      eventCache.clear();
      active = true;
    }
  }

  /**
   * deactivates event dispatching
   */
  public void deactivateBus() {
    synchronized (LOCK) {
      active = false;
    }
  }

  /**
   * allows enforcement of event publishing, e.g. after bus activation.
   * if used inappropriately it will lead to DeadEvents
   *
   * @param event
   *  the event to publish
   * @param enforcePublish
   *  the flag indicating whether to enforce publication
   */
  protected void post(final Object event, boolean enforcePublish) {
    if (enforcePublish) {
      postEvent(event);
    } else {
      eventCache.add(event);
    }
  }

  @Override
  public void post(final Object event) {
    synchronized (LOCK) {
      post(event, active);
    }
  }

}
