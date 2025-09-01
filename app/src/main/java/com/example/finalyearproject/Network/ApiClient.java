package com.example.finalyearproject.Network;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
public class ApiClient {
    private static Retrofit retrofit;

    // Change this to your backend host:
    // - Emulator to your local PC server: http://10.0.2.2:8000/
    // - Physical device to your PC: use your PC's LAN IP, e.g. http://192.168.1.10:8000/
    private static final String BASE_URL = "https://ttjbh2k2-5000.inc1.devtunnels.ms/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(log)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
