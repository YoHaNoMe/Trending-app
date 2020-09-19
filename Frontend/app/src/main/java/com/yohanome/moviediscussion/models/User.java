package com.yohanome.moviediscussion.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;


@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private UUID id;

    @ColumnInfo(name = "user_name")
    private String userName;

    private String name;

    private String email;

    private String password;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @Ignore
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Ignore
    public User(String name, String email, String password, String imageUrl){
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public User(String userName, String name, String email, String password, String imageUrl) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public void setId(UUID id) { this.id = id; }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String format() {
        return "id: " + this.getId() + "\n" +
                "Name: " + this.getName() + "\n" +
                "Email: " + this.getEmail() + "\n" +
                "userName: " + this.getUserName() + "\n" +
                "imageUrl: " + this.getImageUrl() + "\n";
    }
}
