package me.sniggle.android.utils.service;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by iulius on 11/03/16.
 */
public abstract class BaseCallback<T> implements Callback<T> {

  interface FailureHandler<T> {

    void handleFailure(Call<T> call, Throwable t);

  }

  private final FailureHandler failureHandler;

  protected BaseCallback() {
    this(null);
  }

  protected BaseCallback(FailureHandler<T> failureHandler) {
    super();
    this.failureHandler = failureHandler;
  }

  @Override
  public void onFailure(Call<T> call, Throwable t) {
    if( failureHandler != null ) {
      failureHandler.handleFailure(call, t);
    }
  }

}
