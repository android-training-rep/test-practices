package com.example.testrep;

import android.app.Application;

import com.example.testrep.user.UserRepository;

import static org.mockito.Mockito.mock;

public class MyApplication extends Application {
    private UserRepository userRepository;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = mock(UserRepository.class);
        }
        return userRepository;
    }
}
