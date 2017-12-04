package com.mattwiduch.bakeit.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates a basic Retrofit REST client for a given class/interface, which returns a service class
 * from the interface.
 */

public class RetrofitClient {

  private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

  private static Retrofit.Builder builder = new Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create());

  private static Retrofit retrofit = builder.build();

  private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
          .setLevel(HttpLoggingInterceptor.Level.BODY);

  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  public static <S> S createService(Class<S> serviceClass) {
    // Add logging interceptor only if there isn't one yet
    if (!httpClient.interceptors().contains(interceptor)) {
      httpClient.addInterceptor(interceptor);
      builder.client(httpClient.build());
      retrofit = builder.build();
    }
    return retrofit.create(serviceClass);
  }
}
