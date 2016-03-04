package me.sniggle.android.utils.application;

import android.content.Context;

import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

import me.sniggle.android.utils.otto.ActivatorBus;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Simple context that assembles the main dependencies in a ready-to-go state
 *
 * @author iulius
 * @since 1.0
 *
 * @param <HttpService>
 *   the retrofit Http API Service
 * @param <Database>
 *   the Couchbase Object
 * @param <AppBus>
 *   the event bus
 */
public abstract class BaseContext<HttpService, Database extends BaseCouchbase, AppBus extends ActivatorBus> {

  private final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
  private final Context context;
  private final Class<HttpService> httpServiceClass;
  private Manager couchbaseManager;
  private Retrofit retrofit;
  private HttpService httpService;
  private AppBus bus;
  private Database database;

  /**
   *
   * @param context
   *  the Android context
   * @param httpServiceClass
   *  the retrofit http service interface
   */
  protected BaseContext(Context context, Class<HttpService> httpServiceClass) {
    this.httpServiceClass = httpServiceClass;
    this.context = context;
  }

  /**
   * create the appropriate event bus for your app
   *
   * @return the app's event bus
   */
  protected abstract AppBus createBus();

  /**
   * configure the HTTP client here, e.g. ensure the provision of crendentials here
   *
   * @param httpClientBuilder
   *  the HTTP client builder
   */
  protected void configureHttpClient(OkHttpClient.Builder httpClientBuilder) {

  }

  /**
   * configure Retrofit here
   *
   * @param retrofitBuilder
   *  the retrofit builder
   * @param httpClient
   *  the provided HTTP client
   * @return the Retrofit instance
   */
  protected abstract Retrofit configureRetrofit(Retrofit.Builder retrofitBuilder, OkHttpClient httpClient);

  /**
   * create your Couchbase database here
   *
   * @param manager
   *  the Couchbase Manager
   * @return the database access wrapper
   */
  protected abstract Database createDatabase(Manager manager);

  /**
   * configure dependencies
   */
  protected void preCreate() {
    configureHttpClient(httpClientBuilder);
    retrofit = configureRetrofit(new Retrofit.Builder(), httpClientBuilder.build());
  }

  /**
   * create dependencies
   */
  protected void create() {
    bus = createBus();
    if( retrofit != null ) {
      httpService = retrofit.create(httpServiceClass);
    }
    try {
      couchbaseManager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
      database = createDatabase(couchbaseManager);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * perform additional configurations
   */
  protected  void postCreate() {

  }

  /**
   * life cycle method to be called upon application start
   */
  public void onCreate() {
    preCreate();
    create();
    postCreate();
  }

  /**
   * pre cleanup actions
   */
  protected void preTerminate() {

  }

  /**
   * clean up
   */
  protected void terminate() {
    database.close();
    couchbaseManager.close();
  }

  /**
   * additional clean up actions
   */
  protected void postTerminate() {

  }

  /**
   * life cycle method called upon application termination
   */
  public void onTerminate() {
    preTerminate();
    terminate();
    postTerminate();
  }

  /*
   * the accessors to main dependencies
   */

  /**
   *
   * @return the Android context
   */
  public Context getContext() {
    return context;
  }

  /**
   *
   * @return the app's event bus
   */
  public AppBus getBus() {
    return bus;
  }

  /**
   *
   * @return the retrofit API service
   */
  public HttpService getHttpService() {
    return httpService;
  }

  /**
   *
   * @return the Couchbase wrapper
   */
  public Database getDatabase() {
    return database;
  }


}
