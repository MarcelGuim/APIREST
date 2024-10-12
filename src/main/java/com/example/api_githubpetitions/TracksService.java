package com.example.api_githubpetitions;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TracksService {
    @GET("tracks")
    Call<List<Track>> listTracks();

    @GET("tracks/{id}")
    Call<List<Track>> getTrack(@Path("id") String id);

    @DELETE("tracks/{id}")
    Call<Void> deleteTrack(@Path("id") String id);

    @PUT("tracks")
    Call<Void> putTrack(@Body Track track);

    @POST("tracks")
    Call<Void> postTrack(@Body Track track);
}
