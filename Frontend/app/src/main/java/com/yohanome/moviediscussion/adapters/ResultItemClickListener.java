package com.yohanome.moviediscussion.adapters;

import android.widget.ImageView;

import com.yohanome.moviediscussion.models.Movie;

public interface ResultItemClickListener {
    void onResultItemClick(int pos, Movie resultItem, ImageView sharedImageView);
    void onFavouriteImageViewClick(int pos, Movie resultItem, ImageView favouriteImageView);
}
