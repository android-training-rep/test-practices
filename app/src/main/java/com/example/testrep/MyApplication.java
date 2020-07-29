package com.example.testrep;

import android.app.Application;

import androidx.room.Room;

import com.example.testrep.user.DBUserDataSource;
import com.example.testrep.user.UserRepository;

public class MyApplication extends Application {

    private UserRepository userRepository;
    @Override
    public void onCreate() {
        super.onCreate();
        userRepository = new UserRepository(userDataSource());
    }
    public DBUserDataSource userDataSource() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, this.getClass().getSimpleName()).build();
        return db.userDBDataSource();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
