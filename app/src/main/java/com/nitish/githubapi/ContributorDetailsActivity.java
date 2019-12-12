package com.nitish.githubapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nitish.githubapi.adapters.RepositorysRecyclerViewAdapter;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContributorDetailsActivity extends AppCompatActivity {

    RecyclerView repositoryRecyclerView;
    ImageView avatar;

    MyRetrofit retrofit;
    Repos repos;
    String userName="";
    Toolbar toolbar;
    RepositorysRecyclerViewAdapter repositorysRecyclerViewAdapter;
    List<RepositoryApiResponse> repositoryApiList;
    int maxSize=10;
    ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributor_details);

        avatar=findViewById(R.id.avatar);
        repositoryRecyclerView=findViewById(R.id.repository_recycler_view);
        pBar=findViewById(R.id.pBar);
        toolbar=findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v->finish());

        retrofit=MyRetrofit.getInstance();
        repos=retrofit.getApiRepos();

        if(getIntent().getExtras() == null)
            return;

        userName=getIntent().getExtras().getString("userName");

        getRepos();

    }

    public void getRepos(){

        pBar.setVisibility(View.VISIBLE);

        repos.getUserRepos(userName).enqueue(new Callback<List<RepositoryApiResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Response<List<RepositoryApiResponse>> response) {
                runOnUiThread(()->{
                    pBar.setVisibility(View.GONE);

                    if(response.body() != null){
                        Collections.sort(response.body(), (o1, o2) -> Integer.compare(o2.getWatchersCount(), o1.getWatchersCount()));

                        if (response.body().size() < 10) {
                            maxSize = response.body().size();
                        }

                        repositoryApiList=new ArrayList<>();
                        for(int i=0;i<maxSize;i++) repositoryApiList.add(response.body().get(i));

                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ContributorDetailsActivity.this, RecyclerView.VERTICAL,false);
                        repositoryRecyclerView.setLayoutManager(linearLayoutManager );
                        repositorysRecyclerViewAdapter =new RepositorysRecyclerViewAdapter(ContributorDetailsActivity.this,repositoryApiList);
                        repositoryRecyclerView.setAdapter(repositorysRecyclerViewAdapter);
                    }

                });
            }

            @Override
            public void onFailure(@NonNull Call<List<RepositoryApiResponse>> call,@NonNull Throwable t) {
                pBar.setVisibility(View.GONE);

                System.out.println("repository list api failure::::"+t.getMessage());
                System.out.println("repository list api failure::::"+t.getCause().getCause());
                Toast.makeText(ContributorDetailsActivity.this,"SomeThing went wrong",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
