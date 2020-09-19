package com.yohanome.moviediscussion.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FindModel {

    @SerializedName("movie_results")
    private List<Movie> movieResults;

    @SerializedName("tv_results")
    private List<Movie> tvResults;

    public List<Movie> getMovieResults() {
        return movieResults;
    }

    public List<Movie> getTvResults() {
        return tvResults;
    }
}
