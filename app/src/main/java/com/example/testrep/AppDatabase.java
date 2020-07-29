package com.example.testrep;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.testrep.user.DBUserDataSource;
import com.example.testrep.user.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DBUserDataSource userDBDataSource();
}
