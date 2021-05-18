package com.example.testgithubusersexample;

import android.util.Log;
import android.view.Display;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    public interface Listener {
        public void onUsersLoaded(List<User> users);

        public void onError(String message);
    }

    private GitHubService gitHubService;
    private Listener listener;
    private Disposable disposable;

    public MainPresenter(GitHubService gitHubService, Listener listener) {
        this.gitHubService = gitHubService;
        this.listener = listener;
    }

    public void loadUsers() {
        disposable = gitHubService.listUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e(MainPresenter.class.getName(), error.toString()))
                .doOnSuccess(users -> Log.d(MainPresenter.class.getName(), users.toString()))
                .subscribe(users -> listener.onUsersLoaded(users),//подпис на Single
                        error -> listener.onError(error.getMessage()));
    }

    public void stopLoading() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
