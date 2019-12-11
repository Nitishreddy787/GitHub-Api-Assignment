package com.nitish.githubapi.services;

import com.google.gson.JsonObject;
import com.nitish.githubapi.beans.RepositoryApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Repos {

  @Headers({"Content-Type: application/json"})
  @GET("/users/{username}/repos")
  Call<List<RepositoryApiResponse>> getUserRepos(@Path("username") String userName);

}
