package com.yohanome.moviediscussion.db;

import androidx.room.TypeConverter;

import java.util.UUID;

public class Converters {
    @TypeConverter
    public static UUID fromString(String id){
        return id == null ? null : UUID.fromString(id);
    }

    @TypeConverter
    public static String toString(UUID id){
        return id == null ? null : id.toString();
    }
}
