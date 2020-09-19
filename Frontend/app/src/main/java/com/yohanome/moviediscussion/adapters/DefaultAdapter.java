package com.yohanome.moviediscussion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yohanome.moviediscussion.R;
import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.Movie;
import com.yohanome.moviediscussion.models.Result;
import com.yohanome.moviediscussion.movieutils.NetworkUtils;

import java.util.List;

public class DefaultAdapter extends RecyclerView.Adapter<DefaultAdapter.DefaultViewHolder> {
    private Result result;
    private ResultItemClickListener resultItemClickListener;

    public DefaultAdapter(ResultItemClickListener resultItemClickListener) {
        this.result = null;
        this.resultItemClickListener = resultItemClickListener;
    }

    @NonNull
    @Override
    public DefaultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DefaultViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.default_section_recycler_view_item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultViewHolder holder, int position) {
        if (result != null) {
            Movie result = this.result.getResults().get(position);

            if(result.getIsFavourite() == 1)
                holder.favouriteImageView.setImageResource(R.drawable.ic_baseline_favorite_24);
            else
                holder.favouriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);

//            holder.textView.setText(result.getName() + position);
            Picasso.get()
                    .load(NetworkUtils.generateImageUrl(result.getPosterPath()))
                    .into(holder.thumbnail);

            ViewCompat.setTransitionName(holder.thumbnail, String.valueOf(result.getId()));

            holder.itemView.setOnClickListener(v ->
                    resultItemClickListener.onResultItemClick(position, result, holder.thumbnail)
            );

            holder.favouriteImageView.setOnClickListener(v->{
                resultItemClickListener.onFavouriteImageViewClick(position, result, holder.favouriteImageView);
            });

        }
    }

    @Override
    public int getItemCount() {
        if (result != null && result.getResults().size() != 0)
            return result.getResults().size();
        return 0;
    }

    public void setResult(Result result) {
        this.result = result;
        notifyDataSetChanged();
    }


    public static class DefaultViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        RecyclerView recyclerView;
        ImageView favouriteImageView;
        public DefaultViewHolder(View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.default_recyclerView);
            thumbnail = itemView.findViewById(R.id.default_section_thumbnail_imageView);
            favouriteImageView = itemView.findViewById(R.id.default_section_favourite_image_view);
        }
    }

}
