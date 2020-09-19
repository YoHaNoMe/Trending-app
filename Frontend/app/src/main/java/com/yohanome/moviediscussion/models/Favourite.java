package com.yohanome.moviediscussion.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class Favourite implements Parcelable {

    private int id;

    @SerializedName("movie_tv_id")
    private int movieTvId;

    @SerializedName("media_type")
    private String mediaType;

    @Ignore
    public Favourite(int favId, String mediaType){
        this.movieTvId = favId;
        this.mediaType = mediaType;
    }

    protected Favourite(Parcel in) {
        id = in.readInt();
        movieTvId = in.readInt();
        mediaType = in.readString();
    }

    public static final Creator<Favourite> CREATOR = new Creator<Favourite>() {
        @Override
        public Favourite createFromParcel(Parcel in) {
            return new Favourite(in);
        }

        @Override
        public Favourite[] newArray(int size) {
            return new Favourite[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getMovieTvId() {
        return movieTvId;
    }

    public String getMediaType() { return mediaType; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(movieTvId);
        dest.writeString(mediaType);
    }

    public String format(){
        return "id: " + this.getId() + "\n" +
                "movie_tv_id: " + this.getMovieTvId() + "\n" +
                "media_type: " + this.getMediaType() + "\n";
    }
}
