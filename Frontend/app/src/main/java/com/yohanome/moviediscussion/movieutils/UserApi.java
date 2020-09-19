package com.yohanome.moviediscussion.movieutils;

import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.ServerResponse;
import com.yohanome.moviediscussion.models.User;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/users/{id}")
    Call<ServerResponse> getUser(@Path("id") String id);

    @POST("/register")
    Call<ServerResponse> registerUser(@Body User user);

    @POST("/login")
    Call<ServerResponse> loginUser(@Body User user);

    @PATCH("/users/{id}")
    Call<ServerResponse> updateUser(@Path("id") String id, @Body User user);

    @POST("/logout")
    Call<ServerResponse> logoutUser();

    @GET("/users/{id}/favourites")
    Call<ServerResponse> getFavourites(@Path("id") String id);

    @POST("/users/{id}/favourites")
    Call<ServerResponse> postFavourite(@Path("id") String id, @Body Favourite favourite);

    @DELETE("/users/{id}/favourites/{fav_id}")
    Call<ServerResponse> deleteFavourite(@Path("id") String id, @Path("fav_id") int favId);
}