package com.yohanome.moviediscussion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.models.Result;
import com.yohanome.moviediscussion.movieutils.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Result movies;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(movies != null) {
            Movie movie = movies.getResults().get(position);
            Picasso.get()
                    .load(NetworkUtils.generateImageUrl(movie.getPosterPath()))
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if(movies != null && movies.getResults().size() != 0)
            return movies.getResults().size();
        return 2;
    }

    public void setMovies(Result movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        ImageView thumbnail;
        public MovieViewHolder(View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.movie_recyclerView);
            thumbnail = itemView.findViewById(R.id.item_list_imageView);
        }
    }
}
