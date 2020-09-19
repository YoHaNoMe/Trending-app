package com.yohanome.moviediscussion.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.movieutils.NetworkUtils;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private List<Movie> movies;

    public FavouriteAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.favourite_recycler_view_item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        if(movies != null) {
            Movie movie = movies.get(position);
            Picasso.get()
                    .load(NetworkUtils.generateImageUrl(movie.getPosterPath()))
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null)
            return movies.size();
        return 0;
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.favourite_item_list_imageView);
        }
    }
}


