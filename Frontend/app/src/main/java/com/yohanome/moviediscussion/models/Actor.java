package com.yohanome.moviediscussion.models;

public class Actor {

    private int id;
    private String name;
    private String profileImage;

    public Actor(){

    }

    public Actor(int id, String name, String profileImage){
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
