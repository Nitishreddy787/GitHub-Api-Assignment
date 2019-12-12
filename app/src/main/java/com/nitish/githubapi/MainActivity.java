package com.nitish.githubapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import com.google.gson.JsonObject;
import com.nitish.githubapi.adapters.RepositorysRecyclerViewAdapter;
import com.nitish.githubapi.adapters.SearchRepositoryRecyclerViewAdapter;
import com.nitish.githubapi.beans.ItemsItem;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.beans.SearchApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.ArrayList;
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
  SearchRepositoryRecyclerViewAdapter searchRepositoryRecyclerViewAdapter;

  SearchView searchRepositories;
  String[] searchQueries;
  List<ItemsItem> repositoryApiList;
  int maxSize=10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView=findViewById(R.id.recycler_view);
    searchRepositories=findViewById(R.id.search_repositories);

    retrofit=MyRetrofit.getInstance();
    repos=retrofit.getApiRepos();

    searchRepositories.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {

        searchRepository(query);


        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

  }

  public void searchRepository(String query){

    repos.getSearchResult(query).enqueue(new Callback<SearchApiResponse>() {
      @Override
      public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
        if (response.body() != null) {
          System.out.println("new api:::"+response.body().getItems().get(0).getName());

          
          Collections.sort(response.body().getItems(), (o1, o2) -> Integer.compare(o2.getWatchersCount(), o1.getWatchersCount()));

          if (response.body().getItems().size() < 10) {
            maxSize = response.body().getItems().size();
          }

          repositoryApiList=new ArrayList<>();
          for(int i=0;i<maxSize;i++) repositoryApiList.add(response.body().getItems().get(i));

          LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false);
          recyclerView.setLayoutManager(linearLayoutManager );
          searchRepositoryRecyclerViewAdapter =new SearchRepositoryRecyclerViewAdapter(MainActivity.this,repositoryApiList);
          recyclerView.setAdapter(searchRepositoryRecyclerViewAdapter);
        }
      }

      @Override
      public void onFailure(Call<SearchApiResponse> call, Throwable t) {

      }
    });
  }


  /*public void getRepos(){

    repos.getUserRepos("ZacSweers").enqueue(new Callback<List<RepositoryApiResponse>>() {
      @Override
      public void onResponse(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Response<List<RepositoryApiResponse>> response) {
          runOnUiThread(()->{

            if(response.body() != null){
              Collections.sort(response.body(), (o1, o2) -> Integer.compare(o2.getWatchersCount(), o1.getWatchersCount()));

              if (response.body().size() < 10) {
                maxSize = response.body().size();
              }

              repositoryApiList=new ArrayList<>();
              for(int i=0;i<maxSize;i++) repositoryApiList.add(response.body().get(i));

              LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false);
              recyclerView.setLayoutManager(linearLayoutManager );
              repositorysRecyclerViewAdapter =new RepositorysRecyclerViewAdapter(MainActivity.this,repositoryApiList);
              recyclerView.setAdapter(repositorysRecyclerViewAdapter);
            }

          });
      }

      @Override
      public void onFailure(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Throwable t) {
        System.out.println("repository list api failure::::"+t.getMessage());
        System.out.println("repository list api failure::::"+t.getCause().getCause());
      }
    });
  }*/

}
