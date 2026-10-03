package ru.mirea.samsonova.cloudid.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WikiApi {
    @GET("page/summary/{title}")
    Call<WikiSummary> summary(@Path("title") String title);
}
