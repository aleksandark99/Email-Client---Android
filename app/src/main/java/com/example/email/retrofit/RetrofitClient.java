package com.example.email.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//26.192.233.126
    private static Retrofit retrofit;


    private static final String BASE_URL = "http://10.0.2.2:8080/";

    //tijana
    //private static final String BASE_URL = "http://192.168.0.151:8080/";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
            .connectTimeout(3600, TimeUnit.SECONDS)
                    .readTimeout(3600,TimeUnit.SECONDS).build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
