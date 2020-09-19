package com.yohanome.moviediscussion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.Result;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TRENDING_SECTION = 0;
    private static final int MOVIE_SECTION = 1;
    private static final int SERIES_SECTION = 2;
    private Context context;
    private TrendingAdapter trendingAdapter;
    private DefaultAdapter defaultAdapter;
    private MovieAdapter movieAdapter;
    private SeriesAdapter seriesAdapter;

    public MainAdapter(Context context,
                       TrendingAdapter trendingAdapter,
                       DefaultAdapter defaultAdapter,
                       MovieAdapter movieAdapter,
                       SeriesAdapter seriesAdapter) {
        this.context = context;
        this.trendingAdapter = trendingAdapter;
        this.defaultAdapter = defaultAdapter;
        this.movieAdapter = movieAdapter;
        this.seriesAdapter = seriesAdapter;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TRENDING_SECTION:
                return new TrendingAdapter.TrendingViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.trending_section_recycler_view, parent, false)
                );
            case MOVIE_SECTION:
                return new MovieAdapter.MovieViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.movie_section_recycler_view, parent, false)
                );
            case SERIES_SECTION:
                return new SeriesAdapter.SeriesViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.series_section_recycler_view, parent, false)
                );
            default:
                return new DefaultAdapter.DefaultViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.default_section_recycler_view, parent, false)
                );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TRENDING_SECTION:
                ((TrendingAdapter.TrendingViewHolder) holder).recyclerView.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                );
                ((TrendingAdapter.TrendingViewHolder) holder).recyclerView.setAdapter(trendingAdapter);
                break;
            case MOVIE_SECTION:
                ((MovieAdapter.MovieViewHolder) holder).recyclerView.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                );
                ((MovieAdapter.MovieViewHolder) holder).recyclerView.setAdapter(movieAdapter);
                break;
            case SERIES_SECTION:
                ((SeriesAdapter.SeriesViewHolder) holder).recyclerView.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                );
                ((SeriesAdapter.SeriesViewHolder) holder).recyclerView.setAdapter(seriesAdapter);
                break;
            default:
                ((DefaultAdapter.DefaultViewHolder) holder).recyclerView.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                );
                ((DefaultAdapter.DefaultViewHolder) holder).recyclerView.setAdapter(defaultAdapter);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TRENDING_SECTION;
            case 1:
                return MOVIE_SECTION;
            case 2:
                return SERIES_SECTION;
            default:
                return -1;
        }
    }

    public void setResult(Result result) {
        defaultAdapter.setResult(result);
    }

    public void setTrending(Result trending) {
        trendingAdapter.setTrending(trending);
    }

    public void setMovies(Result movies) {
        movieAdapter.setMovies(movies);
    }

    public void setSeries(Result series) {
        seriesAdapter.setSeries(series);
    }
}