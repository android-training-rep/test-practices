package com.example.testrep.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface DBUserDataSource {
    @Query("SELECT * FROM users WHERE username = :name")
    Maybe<User> findByName(String name);

    @Insert
    Completable save(User user);
}
