package com.example.danboard.lab9.service;


import com.example.danboard.lab9.model.Github;
import com.example.danboard.lab9.model.Repos;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Danboard on 17-12-12.
 */

public interface GithubService {
    @GET("/users/{user}/repos")
    Observable<List<Repos>> getRepos(@Path("user") String user);

    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);
}
