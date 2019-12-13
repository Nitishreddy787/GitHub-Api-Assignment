package com.nitish.githubapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nitish.githubapi.adapters.ContributorsListGridViewAdapter;
import com.nitish.githubapi.beans.ContributorsApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nitish.githubapi.InAppBrowserActivity.WEBSITE_ADDRESS;
import androidx.annotation.NonNull;

public class RepositoryDetailsActivity extends AppCompatActivity {

    GridView contributorsLayout;
    Toolbar toolbar;
    TextView name;
    TextView projectUrl;
    TextView description;
    ImageView image;
    MyRetrofit retrofit;
    Repos repos;
    String userName="";
    String projectName="";
    TextView contributors;

    ContributorsListGridViewAdapter contributorsListGridViewAdapter;
    ProgressBar pBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_details);

        image=findViewById(R.id.image);
        name=findViewById(R.id.name);
        projectUrl=findViewById(R.id.project_url);
        description=findViewById(R.id.description);
        contributorsLayout=findViewById(R.id.contributors_layout);
        toolbar=findViewById(R.id.toolbar);
        pBar=findViewById(R.id.pBar);
        contributors=findViewById(R.id.contributors);

        retrofit=MyRetrofit.getInstance();
        repos=retrofit.getApiRepos();

        toolbar.setNavigationOnClickListener(v->finish());


        if(getIntent().getExtras() == null)
            return;

        userName=getIntent().getExtras().getString("loginId");
        projectName=getIntent().getExtras().getString("projectName");

        Glide.with(RepositoryDetailsActivity.this).load(getIntent().getExtras().getString("image")).placeholder(R.drawable.image_loading_horizontal).into(image);
        name.setText("Name: " + getIntent().getExtras().getString("name"));
        projectUrl.setText("Project Url: " + getIntent().getExtras().getString("projectURL"));
        description.setText("Description: " + (getIntent().getExtras().getString("description").equalsIgnoreCase("null") ? "NA" : getIntent().getExtras().getString("description")));

        projectUrl.setOnClickListener(view->{
            Intent inAppBrowser=new Intent(RepositoryDetailsActivity.this,InAppBrowserActivity.class);
            inAppBrowser.putExtra(WEBSITE_ADDRESS,getIntent().getExtras().getString("projectURL"));
            startActivity(inAppBrowser);
        });

        contributors();
    }


    public void contributors(){

        pBar.setVisibility(View.VISIBLE);

        repos.getContributors(userName,projectName).enqueue(new Callback<List<ContributorsApiResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ContributorsApiResponse>> call,@NonNull Response<List<ContributorsApiResponse>> response) {
                runOnUiThread(()->{
                    try{

                        System.out.println("repo details:::"+response.body());

                        pBar.setVisibility(View.GONE);

                        if(response.body() != null){
                            if(!(response.body().size() > 0)){
                                contributors.setText(getResources().getText(R.string.no_contributors));
                            }else{

                                contributorsListGridViewAdapter = new ContributorsListGridViewAdapter(RepositoryDetailsActivity.this,response.body());
                                contributorsLayout.setAdapter(contributorsListGridViewAdapter);
                            }
                        }


                    }catch (Exception e){

                    }
                });
            }

            @Override
            public void onFailure(Call<List<ContributorsApiResponse>> call, Throwable t) {
                runOnUiThread(()->{
                    pBar.setVisibility(View.GONE);
                    Toast.makeText(RepositoryDetailsActivity.this,"SomeThing went wrong",Toast.LENGTH_SHORT).show();
                });
            }
        });

    }


}
