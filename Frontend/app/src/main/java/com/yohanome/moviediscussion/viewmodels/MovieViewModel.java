package com.yohanome.moviediscussion.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.FindModel;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.models.Result;
import com.yohanome.moviediscussion.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private LiveData<Result> resultLiveData;
    private LiveData<Result> moviesLiveData;
    private LiveData<Result> seriesLiveData;
    private LiveData<List<Movie>> movieTvsLiveData;

    public MovieViewModel() {
        Log.d("MainActivity", "From MovieViewModel Constructor");
        movieRepository = new MovieRepository();
        resultLiveData = movieRepository.getResultLiveData();
        moviesLiveData = movieRepository.getMoviesLiveData();
        seriesLiveData = movieRepository.getSeriesLiveData();
        movieTvsLiveData = movieRepository.getMovieTvsLiveData();
    }


    public void getTrending() {
        movieRepository.getTrending();
    }

    public void getMovies() {
        movieRepository.getMovies();
    }

    public void getSeries() {
        movieRepository.getSeries();
    }

    public void getMovieTvs(List<Favourite> favourites) { movieRepository.getMovieTvs(favourites); }

    public LiveData<Result> getResultLiveData() { return resultLiveData; }

    public LiveData<Result> getMoviesLiveData() {
        return moviesLiveData;
    }

    public LiveData<Result> getSeriesLiveData() {
        return seriesLiveData;
    }

    public LiveData<List<Movie>> getMovieTvsLiveData() { return movieTvsLiveData; }
}
