package com.nitish.githubapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import com.google.gson.JsonObject;
import com.nitish.githubapi.adapters.RepositorysRecyclerViewAdapter;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  //ZacSweers

  MyRetrofit retrofit;
  Repos repos;
  RecyclerView recyclerView;
  RepositorysRecyclerViewAdapter repositorysRecyclerViewAdapter;

  SearchView searchRepositories;
  String[] searchQueries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView=findViewById(R.id.recycler_view);
    searchRepositories=findViewById(R.id.search_repositories);

    retrofit=MyRetrofit.getInstance();
    repos=retrofit.getApiRepos();

    getRepos();
  }


  public void getRepos(){

    repos.getUserRepos("sav007").enqueue(new Callback<List<RepositoryApiResponse>>() {
      @Override
      public void onResponse(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Response<List<RepositoryApiResponse>> response) {
          runOnUiThread(()->{
            try{

              Collections.sort(response.body(), (o1, o2) -> ((Integer)o1.getWatchersCount()).compareTo(o2.getWatchersCount()));


              LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false);
              recyclerView.setLayoutManager(linearLayoutManager );
              repositorysRecyclerViewAdapter =new RepositorysRecyclerViewAdapter(MainActivity.this,response.body());
              recyclerView.setAdapter(repositorysRecyclerViewAdapter);
            }catch (Exception e){
              System.out.println("repository list api exception::::"+e.getMessage());
              System.out.println("repository list api exception::::"+e.getCause());

            }
          });
      }

      @Override
      public void onFailure(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Throwable t) {
        System.out.println("repository list api failure::::"+t.getMessage());
        System.out.println("repository list api failure::::"+t.getCause().getCause());
      }
    });
  }
}
