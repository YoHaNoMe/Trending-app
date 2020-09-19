package com.yohanome.moviediscussion.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.ServerResponse;
import com.yohanome.moviediscussion.models.User;
import com.yohanome.moviediscussion.db.UserDao;
import com.yohanome.moviediscussion.db.UserRoomDatabase;
import com.yohanome.moviediscussion.movieutils.MovieUtils;
import com.yohanome.moviediscussion.movieutils.NetworkUtils;
import com.yohanome.moviediscussion.movieutils.UserApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {

    private UserApi api;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> userLoggedInLiveData = new MutableLiveData<>();
    private LiveData<User> userCachedLiveData;
    private UserDao userDao;
    private MutableLiveData<List<Favourite>> favouriteListLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> isFavourite = new MutableLiveData<>();

    public UserRepository(Application application){
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtils.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(UserApi.class);
        // Initialize Database and Data Access Object (DAO)
        UserRoomDatabase db = UserRoomDatabase.userRoomDatabase(application);
        userDao = db.userDao();
        userCachedLiveData = userDao.getUser();
    }

    public void getUser(String id){
        Log.d("MainFragment", "From getUser");
        api.getUser(id).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(!response.isSuccessful()) {
                    userLiveData.postValue(response.body().getUser());
                    return;
                }

                Log.d("MainFragment", "Successfully getting user");
                userLiveData.postValue(response.body().getUser());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("MainActivity", t.getMessage());
                userLiveData.postValue(null);
            }
        });
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }

    public LiveData<User> loginUser(User user){
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        api.loginUser(user).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(!response.isSuccessful()) {
                    userLiveData.postValue(null);
                    userLoggedInLiveData.postValue(null);
                    Log.d("MainActivity", "Not Successful");
                    return;
                }
                Log.d("MainActivity", "Successful");
                userLiveData.postValue(response.body().getUser());
                insert(response.body().getUser());
                userLoggedInLiveData.postValue(true);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("MainActivity", "Error: " + t.getMessage());
                userLiveData.postValue(null);
                userLoggedInLiveData.postValue(null);
            }
        });
        return userLiveData;
    }

    public LiveData<User> registerUser(User user){
        MutableLiveData<User> userRegisterLiveData = new MutableLiveData<>();
        api.registerUser(user).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NotNull Call<ServerResponse> call, @NotNull Response<ServerResponse> response) {
                if(!response.isSuccessful()){
                    userRegisterLiveData.postValue(null);
                    userLoggedInLiveData.postValue(null);
                    return;
                }

                userRegisterLiveData.postValue(response.body().getUser());
                insert(response.body().getUser());
                userLoggedInLiveData.postValue(true);
            }

            @Override
            public void onFailure(@NotNull Call<ServerResponse> call, @NotNull Throwable t) {
                Log.d("MainActivity", "From OnFailure: " + t.getMessage());
                userRegisterLiveData.postValue(null);
                userLoggedInLiveData.postValue(null);
            }
        });
        return userRegisterLiveData;
    }

    public void updateUser(String id, User user){
        api.updateUser(id, user).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(!response.isSuccessful()){
                    userLiveData.postValue(null);
                    return;
                }

                userLiveData.postValue(response.body().getUser());
                update(response.body().getUser());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                userLiveData.postValue(null);
            }
        });
    }

    public void logoutUser(){
        userLoggedInLiveData.postValue(false);
        userLiveData.postValue(null);
        delete();
    }

    public LiveData<Boolean> getUserLoggedInLiveData() {return userLoggedInLiveData; }

    // Insert User to Database
    public void insert(User user){
        UserRoomDatabase.databaseWriterExecutor.execute(()->{
            userDao.insert(user);
        });
    }

    // Delete User from Database
    public void delete(){
        UserRoomDatabase.databaseWriterExecutor.execute(()->{
            userDao.delete();
        });
    }

    // Update User from Database
    public void update(User user){
        UserRoomDatabase.databaseWriterExecutor.execute(()->{
            userDao.update(user);
        });
    }

    public LiveData<User> getCachedUserLiveData() { return userCachedLiveData; }

    public void postFavourite(String userId, Favourite favourite){
        api.postFavourite(userId, favourite).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(!response.isSuccessful()) {
                    isFavourite.postValue(null);
                    return;
                }
                Log.d("MainFragment", "Inside Like Movie122");
                Log.d("MainFragment", "Inside Like: " + response.body().getFavourite().format());
                isFavourite.postValue(1);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("MainFragment", "Favourite Error: " + t.getMessage());
                isFavourite.postValue(null);
            }
        });
    }

    public void getFavourites(String userId){
        api.getFavourites(userId).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(!response.isSuccessful()){
                    favouriteListLiveData.postValue(null);
                    return;
                }

                favouriteListLiveData.postValue(response.body().getFavourites());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                favouriteListLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<Favourite>> getFavouritesLiveData() { return favouriteListLiveData; }

    public void deleteFavourite(String userId, int favId){
        api.deleteFavourite(userId, favId).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(!response.isSuccessful()){
                    isFavourite.postValue(null);
                    return;
                }

                Log.d("MainFragment", "From Delete Favourite");
                isFavourite.postValue(0);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                isFavourite.postValue(null);
            }
        });
    }

    public LiveData<Integer> getIsFavourite() { return isFavourite; }
}
