package com.yohanome.moviediscussion.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse {

    @SerializedName("status_code")
    private int statusCode;

    private boolean success;

    private User user;

    private Favourite favourite;

    private List<Favourite> favourites;

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public List<Favourite> getFavourites() { return favourites; }

    public Favourite getFavourite() { return favourite; }

    public String format(){
        return "status_code: " + this.getStatusCode() + "\n" +
                "success: " + this.isSuccess() + "\n" +
                "user: " + this.getUser().format() + "\n" +
                "favourite: " + this.getFavourite().format() + "\n";
    }
}
