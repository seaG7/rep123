package ru.mirea.samsonova.cloudid.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class WikiClient {
    private static final String BASE_URL = "https://ru.wikipedia.org/api/rest_v1/";
    private static final String AGENT = "CloudID/1.0 (ru.mirea.samsonova.cloudid; MIREA coursework)";

    private WikiClient() {
    }

    public static WikiApi create() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("User-Agent", AGENT)
                            .header("Api-User-Agent", AGENT)
                            .build();
                    return chain.proceed(request);
                })
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WikiApi.class);
    }
}
