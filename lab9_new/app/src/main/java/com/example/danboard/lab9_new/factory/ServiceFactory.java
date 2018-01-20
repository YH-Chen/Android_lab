package com.example.danboard.lab9_new.factory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Danboard on 17-12-12.
 */

public class ServiceFactory {
    private static OkHttpClient createOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)   // 连接超时
                .readTimeout(30, TimeUnit.SECONDS)      // 读超时
                .writeTimeout(10, TimeUnit.SECONDS)     // 写超时
                .build();
    }

    private static Retrofit createRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }

    public static Retrofit getRetrofit(String url) {
        return createRetrofit(url);
    }
}
