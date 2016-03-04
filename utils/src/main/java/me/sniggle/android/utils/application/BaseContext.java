package me.sniggle.android.utils.application;

import android.content.Context;

import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

import me.sniggle.android.utils.otto.ActivatorBus;
import me.sniggle.android.utils.otto.MainThreadBus;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by iulius on 04/03/16.
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

  protected BaseContext(Context context, Class<HttpService> httpServiceClass) {
    this.httpServiceClass = httpServiceClass;
    this.context = context;
  }

  protected abstract AppBus createBus();

  protected void configureHttpClient(OkHttpClient.Builder httpClientBuilder) {

  }

  protected Retrofit configureRetrofit(Retrofit.Builder retrofitBuilder, OkHttpClient httpClient) {
    return null;
  }

  protected abstract Database createDatabase(Manager manager);

  protected void preCreate() {
    configureHttpClient(httpClientBuilder);
    retrofit = configureRetrofit(new Retrofit.Builder(), httpClientBuilder.build());
  }

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

  protected  void postCreate() {

  }

  public void onCreate() {
    preCreate();
    create();
    postCreate();
  }

  protected void preTerminate() {

  }

  protected void terminate() {
    database.close();
    couchbaseManager.close();
  }

  protected void postTerminate() {

  }

  public void onTerminate() {
    preTerminate();
    terminate();
    postTerminate();
  }

  public Context getContext() {
    return context;
  }

  public AppBus getBus() {
    return bus;
  }

  public HttpService getHttpService() {
    return httpService;
  }

  public Database getDatabase() {
    return database;
  }


}
