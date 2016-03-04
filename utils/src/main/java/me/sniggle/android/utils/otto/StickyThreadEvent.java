package me.sniggle.android.utils.otto;

/**
 * An event that you can enforce to be executed on the MainThread
 *
 * @author iulius
 * @since 1.0
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
