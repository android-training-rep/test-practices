package com.example.testrep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testrep.user.User;
import com.example.testrep.user.UserRepository;
import com.example.testrep.utils.Encryptor;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "Login Activity";
    LoginViewModel model;
    UserRepository userRepository;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        model = obtainViewModel();

        Button loginBtn = findViewById(R.id.login);
        Button createBtn = findViewById(R.id.create);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        model.getLoginResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String loginResult) {
                Log.d(TAG, "---on change---loginResult---" + loginResult);
                Toast.makeText(getApplicationContext(),loginResult,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createUser(String username, String password) {
        User user = new User(username, Encryptor.md5(password));

        userRepository.save(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "save user --- on complete");
                        Toast.makeText(getApplicationContext(),"New User successfully!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "save user --- on error");
                    }
                });
    }

    private LoginViewModel obtainViewModel() {
        userRepository = (((MyApplication) getApplicationContext())).getUserRepository();
        LoginViewModel loginViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LoginViewModel.class);
        loginViewModel.setUserRepository(userRepository);
        return loginViewModel;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}