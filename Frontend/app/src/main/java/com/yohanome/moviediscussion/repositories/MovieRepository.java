package com.yohanome.moviediscussion.repositories;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.models.Result;
import com.yohanome.moviediscussion.movieutils.MovieApi;
import com.yohanome.moviediscussion.movieutils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private MovieApi api;
    private MutableLiveData<Result> result = new MutableLiveData<>();
    private MutableLiveData<Result> movies = new MutableLiveData<>();
    private MutableLiveData<Result> series = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> movieTvs = new MutableLiveData<>();

    public MovieRepository() {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(NetworkUtils.attachApiKeyToEveryRequest())
                .build();
        // Create api
        api = retrofit.create(MovieApi.class);
    }

    public void getTrending() {
        api.getTrending().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                result.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                result.postValue(null);
            }
        });
    }

    public void getMovies() {
        api.getMovies().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                movies.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    public void getSeries() {
        api.getTvs().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                series.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    public void getMovieTvs(List<Favourite> favourites) {
        new MovieTvsAsyncTask().execute(favourites);
    }

    private class MovieTvsAsyncTask extends AsyncTask<List<Favourite>, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(List<Favourite>... favourites) {
            List<Movie> results = new ArrayList<>();
            for (Favourite favourite : favourites[0]) {
                try {
                    if (favourite.getMediaType().equals("movie")) {
                        results.add(api.findMovie(favourite.getMovieTvId()).execute().body());
                    } else {
                        Log.d("MainContained", favourite.format());
                        results.add(api.findTv(favourite.getMovieTvId()).execute().body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null)
                movieTvs.postValue(movies);
        }
    }

    public LiveData<Result> getResultLiveData() {
        return result;
    }

    public LiveData<Result> getMoviesLiveData() {
        return movies;
    }

    public LiveData<Result> getSeriesLiveData() {
        return series;
    }

    public LiveData<List<Movie>> getMovieTvsLiveData() {
        return movieTvs;
    }

}
