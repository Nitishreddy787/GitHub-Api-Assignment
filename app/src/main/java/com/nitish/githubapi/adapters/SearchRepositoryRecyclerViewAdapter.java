package com.nitish.githubapi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nitish.githubapi.R;
import com.nitish.githubapi.RepositoryDetailsActivity;
import com.nitish.githubapi.beans.CommitsApiResponse;
import com.nitish.githubapi.beans.ItemsItem;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepositoryRecyclerViewAdapter extends RecyclerView.Adapter<SearchRepositoryRecyclerViewAdapter.ViewHolder> {

  private List<ItemsItem> response;
  Context context;
  LayoutInflater mInflater;
  MyRetrofit retrofit;
  Repos repos;

  public SearchRepositoryRecyclerViewAdapter(Context context) {
    this.context=context;
    this.mInflater=LayoutInflater.from(context);

    retrofit=MyRetrofit.getInstance();
    repos=retrofit.getApiRepos();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view=mInflater.inflate(R.layout.repository_list_recycler_view,parent,false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    Glide.with(context).load(response.get(position).getOwner().getAvatarUrl()).placeholder(R.drawable.image_loading_vertical).into(holder.avatar);
    holder.name.setText("Name: "+response.get(position).getName());
    holder.fullName.setText("Full Name: "+response.get(position).getFullName());
    holder.watcher.setText("Watcher Counts: "+String.valueOf(response.get(position).getWatchersCount()));
    holder.commits.setText("Commit Counts: "+response.get(position).getCommitsUrl());


    holder.repositoryCard.setOnClickListener(view->{

      Intent repositoryDetails=new Intent(context, RepositoryDetailsActivity.class);
      repositoryDetails.putExtra("image",response.get(position).getOwner().getAvatarUrl());
      repositoryDetails.putExtra("name",response.get(position).getName());
      repositoryDetails.putExtra("projectURL",response.get(position).getCloneUrl());
      repositoryDetails.putExtra("description",String.valueOf(response.get(position).getDescription()));
      repositoryDetails.putExtra("projectName",response.get(position).getName());
      repositoryDetails.putExtra("loginId",response.get(position).getOwner().getLogin());
      context.startActivity(repositoryDetails);
    });

    commits(position,holder.commits);
  }

  public void setRepositoryList(List<ItemsItem> response) {
    this.response = response;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if(response != null) {
      return response.size();
    }
    return 0;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    View myView;
    ImageView avatar;
    TextView name;
    TextView fullName;
    TextView watcher;
    TextView commits;
    CardView repositoryCard;
    ViewHolder(View v) {
      super(v);
      avatar=v.findViewById(R.id.avatar);
      name=v.findViewById(R.id.project_name);
      fullName=v.findViewById(R.id.project_full_name);
      watcher=v.findViewById(R.id.watcher_count);
      commits=v.findViewById(R.id.commit_count);
      repositoryCard=v.findViewById(R.id.repository_card);

    }
  }

  private void commits(int position, TextView commits){

    repos.commits(response.get(position).getOwner().getLogin(),response.get(position).getName()).enqueue(new Callback<List<CommitsApiResponse>>() {
      @Override
      public void onResponse(Call<List<CommitsApiResponse>> call, Response<List<CommitsApiResponse>> response) {
        ((Activity) context).runOnUiThread(()-> {
          if(response.body() != null){
            commits.setText(String.valueOf(response.body().size()));
          }
        });
      }

      @Override
      public void onFailure(Call<List<CommitsApiResponse>> call, Throwable t) {
        ((Activity) context).runOnUiThread(()-> {

        });
      }
    });

  }

}
