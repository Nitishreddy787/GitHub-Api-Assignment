package com.nitish.githubapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.JsonObject;
import com.nitish.githubapi.adapters.RepositorysRecyclerViewAdapter;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  MyRetrofit retrofit;
  Repos repos;
  RecyclerView recyclerView;
  RepositorysRecyclerViewAdapter repositorysRecyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView=findViewById(R.id.recycler_view);

    retrofit=MyRetrofit.getInstance();
    repos=retrofit.getApiRepos();

    getRepos();
  }


  public void getRepos(){

    repos.getUserRepos("Nitishreddy787").enqueue(new Callback<List<RepositoryApiResponse>>() {
      @Override
      public void onResponse(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Response<List<RepositoryApiResponse>> response) {
          runOnUiThread(()->{
            try{
              System.out.println("Total Repository's::::"+response.body());
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
