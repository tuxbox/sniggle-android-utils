package me.sniggle.android.utils.service;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A convenience class providing a sensible default implementation for a Retrofit Callback
 */
public abstract class BaseCallback<T> implements Callback<T> {

  /**
   * A failure handler for the Callback#onFailure(Call&lt;T>, Throwable) method
   *
   * @param <T>
   *   matches the Callbacks Type
   */
  interface FailureHandler<T> {

    /**
     * called upon failure of the retrofit call
     *
     * @param call
     *  the retrofit call
     * @param t
     *  the throwable object
     */
    void handleFailure(Call<T> call, Throwable t);

  }

  private final FailureHandler failureHandler;

  /**
   * constructor defaulting failure handler to null
   */
  protected BaseCallback() {
    this(null);
  }

  /**
   * constructor that takes a failure handler implementation
   *
   * @param failureHandler
   *  the failure handler to utilize
   */
  protected BaseCallback(FailureHandler<T> failureHandler) {
    super();
    this.failureHandler = failureHandler;
  }

  /**
   * @see Callback&lt;T>#onFailure(Call&lt;T>, Throwable)
   * @param call
   * @param t
   */
  @Override
  public void onFailure(Call<T> call, Throwable t) {
    if( failureHandler != null ) {
      failureHandler.handleFailure(call, t);
    }
  }

}
