package com.yohanome.moviediscussion.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yohanome.moviediscussion.models.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<User> getUser();

    @Insert
    void insert(User user);

    @Query("DELETE FROM users")
    void delete();

    @Update
    void update(User user);
}
