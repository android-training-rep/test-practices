package com.example.testrep;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testrep.user.User;
import com.example.testrep.user.UserRepository;
import com.example.testrep.utils.Encryptor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LoginViewModel extends ViewModel {
    public static final String TAG = "Login ViewModel";
    public static final String INVALID_PASSWORD = "Password is invalid";
    public static final String INVALID_USERNAME = "User does not exist";
    public static final String VALID_INPUT = "Login successfully";
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MutableLiveData<String> loginResult;

    private UserRepository userRepository;

    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<String> getLoginResult() {
        if(loginResult==null) {
            loginResult = new MutableLiveData<String>();
            loginResult.postValue("");
        }
        return loginResult;
    }

    @SuppressLint("CheckResult")
    public void login(String username, final String password) {
        userRepository.findByName(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        loginResult.postValue(INVALID_USERNAME);
                    }
                })
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        if(user == null) {
                            loginResult.postValue(INVALID_USERNAME);
                            return ;
                        }

                        if(user.getPassword().equals(Encryptor.md5(password))) {
                            loginResult.postValue(VALID_INPUT);
                             return ;
                        }
                        loginResult.postValue(INVALID_PASSWORD);
                    }
                });

    }
}
