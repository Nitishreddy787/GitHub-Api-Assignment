package com.nitish.githubapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

public class RepositoryDetailsActivity extends AppCompatActivity {

    GridView contributorsLayout;
    Toolbar toolbar;
    TextView name;
    TextView projectUrl;
    TextView description;
    ImageView image;
    MyRetrofit retrofit;
    Repos repos;

    ContributorsListGridViewAdapter contributorsListGridViewAdapter;

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

        retrofit=MyRetrofit.getInstance();
        repos=retrofit.getApiRepos();

        toolbar.setNavigationOnClickListener(v->finish());


        if(getIntent().getExtras() == null)
            return;


        Glide.with(RepositoryDetailsActivity.this).load(getIntent().getExtras().getString("image")).placeholder(R.drawable.image_loading_horizontal).into(image);
        name.setText("Name: "+getIntent().getExtras().getString("name"));
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

        repos.getContributors("ZacSweers",getIntent().getExtras().getString("projectName")).enqueue(new Callback<List<ContributorsApiResponse>>() {
            @Override
            public void onResponse(Call<List<ContributorsApiResponse>> call, Response<List<ContributorsApiResponse>> response) {
                runOnUiThread(()->{
                    try{
                        contributorsListGridViewAdapter = new ContributorsListGridViewAdapter(RepositoryDetailsActivity.this,response.body());
                        contributorsLayout.setAdapter(contributorsListGridViewAdapter);
                    }catch (Exception e){

                    }
                });
            }

            @Override
            public void onFailure(Call<List<ContributorsApiResponse>> call, Throwable t) {
                runOnUiThread(()->{

                });
            }
        });

    }


}
