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

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {
    private Result trending;

    public TrendingAdapter(){

    }

    public TrendingAdapter(Result trending){
        this.trending = trending;
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrendingViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        if(trending != null) {
            Movie movie = trending.getResults().get(position);
            Picasso.get()
                    .load(NetworkUtils.generateImageUrl(movie.getPosterPath()))
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if(trending != null && trending.getResults().size() != 0)
            return trending.getResults().size();
        return 2;
    }

    public void setTrending(Result trending){
        this.trending = trending;
        notifyDataSetChanged();
    }

    public static class TrendingViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        ImageView thumbnail;
        public TrendingViewHolder(View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.trending_recyclerView);
            thumbnail = itemView.findViewById(R.id.item_list_imageView);
        }
    }
}
