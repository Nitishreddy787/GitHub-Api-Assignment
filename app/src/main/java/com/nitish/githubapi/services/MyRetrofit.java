package com.nitish.githubapi.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit implements Serializable, Constants {


  private static final String TAG = "MyRetrofit";

  private static final String HEADER_CACHE_CONTROL = "Cache-Control";
  private static final String HEADER_PRAGMA = "Pragma";

  private static volatile MyRetrofit mInstance;
  private Context mContext;

  private static volatile Repos apiRepos;

  private MyRetrofit() {
    if (mInstance != null) {
      throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
    }
  }

  public void setContext(Context context) {
    mContext = context;
  }

  public static MyRetrofit getInstance() {

    if (mInstance == null) {
      synchronized (MyRetrofit.class) {
        if (mInstance == null) {
          mInstance = new MyRetrofit();

          HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
          interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

          OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();



          Gson gson = new GsonBuilder()
            .setLenient()
            .create();




          OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(mInstance.provideOfflineCacheInterceptor())
            .addNetworkInterceptor(mInstance.provideCacheInterceptor())
            .cache(mInstance.provideCache());

          Retrofit apiReposApis = new Retrofit.Builder()
            .baseUrl(GIT_HUB)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

          apiRepos = apiReposApis.create(Repos.class);

        }
      }
    }

    return mInstance;

  }

  protected MyRetrofit readResolve() {
    return getInstance();
  }

  public Repos getApiRepos() {
    return apiRepos;
  }


  private Cache provideCache() {
    Cache cache = null;

    try {
      cache = new Cache(new File(mContext.getCacheDir(), "http-cache"),
        10 * 1024 * 1024); // 10 MB
    } catch (Exception e) {
      Log.e(TAG, "Could not create Cache!");
    }

    return cache;
  }

  private Interceptor provideCacheInterceptor() {
    return chain -> {
      Response response = chain.proceed(chain.request());

      CacheControl cacheControl;

      if (isConnected()) {
        cacheControl = new CacheControl.Builder()
          .maxAge(0, TimeUnit.SECONDS)
          .build();
      } else {
        cacheControl = new CacheControl.Builder()
          .maxStale(7, TimeUnit.DAYS)
          .build();
      }

      return response.newBuilder()
        .removeHeader(HEADER_PRAGMA)
        .removeHeader(HEADER_CACHE_CONTROL)
        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
        .build();

    };
  }

  private Interceptor provideOfflineCacheInterceptor() {
    return chain -> {
      Request request = chain.request();
      if (!isConnected()) {
        CacheControl cacheControl = new CacheControl.Builder()
          .maxStale(7, TimeUnit.DAYS)
          .build();

        request = request.newBuilder()
          .removeHeader(HEADER_PRAGMA)
          .removeHeader(HEADER_CACHE_CONTROL)
          .cacheControl(cacheControl)
          .build();
      }
      return chain.proceed(request);
    };
  }


  public boolean isConnected() {
    try {
      android.net.ConnectivityManager e = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeNetwork = e.getActiveNetworkInfo();
      return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    } catch (Exception e) {
      Log.w(TAG, e.toString());
    }

    return false;
  }



}

