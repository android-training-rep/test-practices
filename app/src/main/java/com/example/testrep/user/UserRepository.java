package com.example.testrep.user;

import androidx.room.Insert;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class UserRepository {
    DBUserDataSource userDataSource;
    public UserRepository(DBUserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Maybe<User> findByName(String name){
        return userDataSource.findByName(name);
    }

    public Completable save(User user){
        return userDataSource.save(user);
    }
}
