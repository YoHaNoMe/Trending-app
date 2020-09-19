package com.yohanome.moviediscussion.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yohanome.moviediscussion.models.Favourite;
import com.yohanome.moviediscussion.models.User;
import com.yohanome.moviediscussion.movieutils.MovieUtils;
import com.yohanome.moviediscussion.repositories.UserRepository;

import java.util.List;


public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<User> userLiveData;
    private LiveData<Boolean> userLoggedInLiveData;
    private LiveData<User> userCachedLiveData;
    private LiveData<Integer> isFavouriteLiveData;
    private LiveData<List<Favourite>> favouritesLiveData;

    public UserViewModel(Application application){
        super(application);
        userRepository = new UserRepository(application);
        userLiveData = userRepository.getUserLiveData();
        userLoggedInLiveData = userRepository.getUserLoggedInLiveData();
        userCachedLiveData = userRepository.getCachedUserLiveData();
        isFavouriteLiveData = userRepository.getIsFavourite();
        favouritesLiveData = userRepository.getFavouritesLiveData();
    }
    // Get user from Server
    public void getUser(String id) { userRepository.getUser(id); }

    public LiveData<User> getUserLiveData() { return userLiveData; }

    // Login User
    public LiveData<User> loginUser(User user) { return userRepository.loginUser(user); }

    // Register User
    public LiveData<User> registerUser(User user) { return userRepository.registerUser(user); }

    // Update User
    public void updateUser(String id, User user) { userRepository.updateUser(id, user); }

    // Logout User
    public void logoutUser() {
        userRepository.logoutUser();
    }

    public LiveData<Boolean> getUserLoggedInLiveData() { return userLoggedInLiveData; }

    public void postFavourite(String userId, Favourite favourite) { userRepository.postFavourite(userId, favourite); }

    public void getFavourites(String userId) { userRepository.getFavourites(userId); }

    public LiveData<List<Favourite>> getFavouritesLiveData() { return favouritesLiveData; }

    public void deleteFavourite(String userId, int favId) { userRepository.deleteFavourite(userId, favId); }

    public LiveData<Integer> getIsFavouriteLiveData() { return isFavouriteLiveData; }

    // Get user from Database (Cached User)
    public LiveData<User> getUserCachedLiveData(){ return userCachedLiveData; }
}
