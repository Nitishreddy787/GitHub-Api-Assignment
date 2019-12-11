package com.nitish.githubapi.services;

import com.nitish.githubapi.beans.CommitsApiResponse;
import com.nitish.githubapi.beans.ContributorsApiResponse;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.beans.UserProfileApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface Repos {

  @Headers({"Content-Type: application/json"})
  @GET("/users/{username}/repos")
  Call<List<RepositoryApiResponse>> getUserRepos(@Path("username") String userName);

  @Headers({"Content-Type: application/json"})
  @GET("/repos/{userName}/{projectName}/contributors")
  Call<List<ContributorsApiResponse>> getContributors(@Path("userName") String userName,@Path("projectName") String projectName);

  @Headers({"Content-Type: application/json"})
  @GET("/users/{userName}")
  Call<UserProfileApiResponse> userProfile(@Path("userName") String userName);

  @Headers({"Content-Type: application/json"})
  @GET("/repos/{userName}/{projectName}/commits")
  Call<List<CommitsApiResponse>> commits(@Path("userName") String userName, @Path("projectName") String projectName);
}
