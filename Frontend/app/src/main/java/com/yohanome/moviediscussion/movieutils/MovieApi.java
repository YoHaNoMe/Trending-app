package com.yohanome.moviediscussion.movieutils;

import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.models.Result;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApi {

    @GET("trending/all/week")
    Call<Result> getTrending();

    @GET("discover/movie")
    Call<Result> getMovies();

    @GET("discover/tv")
    Call<Result> getTvs();

    @GET("movie/{id}")
    Call<Movie> findMovie(@Path("id") int id);

    @GET("tv/{id}")
    Call<Movie> findTv(@Path("id") int id);
}
