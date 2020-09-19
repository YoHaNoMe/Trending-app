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

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private Result series;

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeriesViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        if(series != null) {
            Movie series = this.series.getResults().get(position);
            Picasso.get().load(NetworkUtils.generateImageUrl(series.getPosterPath())).into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (series != null && series.getResults().size() != 0)
            return series.getResults().size();
        return 2;
    }

    public void setSeries(Result series) {
        this.series = series;
        notifyDataSetChanged();
    }

    public static class SeriesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        ImageView thumbnail;

        public SeriesViewHolder(View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.series_recyclerView);
            thumbnail = itemView.findViewById(R.id.item_list_imageView);
        }
    }

}
