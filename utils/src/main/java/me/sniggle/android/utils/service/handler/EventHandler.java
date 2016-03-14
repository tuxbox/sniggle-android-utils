package me.sniggle.android.utils.service.handler;

/**
 * Interface defining the lifecycle functions of an EventHandler
 */
public interface EventHandler {

  /**
   * initialization method
   */
  void onCreate();

  /**
   * clean up method
   */
  void onDestroy();

}
