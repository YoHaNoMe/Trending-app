package com.yohanome.moviediscussion.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    private int id;

    private String name;

    private String title;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    private String overview;

    private double popularity;

    @SerializedName("media_type")
    private String mediaType;

    private Integer isFavourite;

    public Movie(){
        this.isFavourite = 0;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        name = in.readString();
        title = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readFloat();
        firstAirDate = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        mediaType = in.readString();
        isFavourite = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Integer getIsFavourite() { return isFavourite; }

    public void setIsFavourite(Integer isFavourite){
        this.isFavourite = isFavourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeInt(voteCount);
        dest.writeFloat(voteAverage);
        dest.writeString(firstAirDate);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeString(mediaType);
        dest.writeInt(isFavourite);
    }

    public String format(){
        return "id: " + this.getId() + "\n" +
                "title: " + this.getTitle() + "\n" +
                "posterPath: " + this.getPosterPath() + "\n";
    }
}
