package com.example.testrep;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.testrep.user.User;
import com.example.testrep.user.UserRepository;
import com.example.testrep.utils.Encryptor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.internal.operators.completable.CompletableCreate;
import io.reactivex.internal.operators.maybe.MaybeCreate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    String invalidPasswordText = "Password is invalid";
    String invalidUsernameText = "Username does not exist";
    String successText = "Login successfully";

    @Test
    public void testLoginButtonText() {
        onView(withText("Login")).check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_invalid_password() {
        MyApplication applicationContext = (MyApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        UserRepository userRepository = applicationContext.getUserRepository();

        User user = new User("username", Encryptor.md5("password"));
        when(userRepository.findByName("username")).thenReturn(new MaybeCreate(emitter -> emitter.onSuccess(user)));

        onView(withId(R.id.username)).perform(typeText("username"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.login)).perform(click());
        onView(withText(invalidPasswordText)).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_username_does_not_exist() {
        MyApplication applicationContext = (MyApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        UserRepository userRepository = applicationContext.getUserRepository();

        User user = new User("username", Encryptor.md5("password"));
        when(userRepository.findByName("username")).thenReturn(new MaybeCreate(emitter -> emitter.onSuccess(user)));

        onView(withId(R.id.username)).perform(typeText("android"));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.login)).perform(click());
        onView(withText(invalidUsernameText)).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_successfully_when_login_given_correct_username_and_password() {
        MyApplication applicationContext = (MyApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        UserRepository userRepository = applicationContext.getUserRepository();

        User user = new User("android", Encryptor.md5("123456"));
        when(userRepository.findByName("android")).thenReturn(new MaybeCreate(emitter -> emitter.onSuccess(user)));

        onView(withId(R.id.username)).perform(typeText("android"));
        onView(withId(R.id.password)).perform(typeText("123456"));
        onView(withId(R.id.login)).perform(click());
        onView(withText(successText)).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_new_user_successfully() {
        MyApplication applicationContext = (MyApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        UserRepository userRepository = applicationContext.getUserRepository();
        String username = "test123";
        String password = "test123";
        User user = new User(username, Encryptor.md5(password));
        when(userRepository.save(user)).thenReturn(new CompletableCreate(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                emitter.onComplete();
            }
        }));
        onView(withId(R.id.username)).perform(typeText("test123"));
        onView(withId(R.id.password)).perform(typeText("test123"));
        onView(withId(R.id.login)).perform(click());
        onView(withText("New User successfully!")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}