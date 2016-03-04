package me.sniggle.android.utils.otto;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class StickyThreadEvent {

  private final boolean stickToMainThread;

  protected StickyThreadEvent() {
    this(true);
  }

  protected StickyThreadEvent(boolean stickToMainThread) {
    this.stickToMainThread = stickToMainThread;
  }

  public boolean isStickToMainThread() {
    return stickToMainThread;
  }

}
