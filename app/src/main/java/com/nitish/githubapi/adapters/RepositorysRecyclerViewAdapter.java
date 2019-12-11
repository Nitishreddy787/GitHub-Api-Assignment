package com.nitish.githubapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.nitish.githubapi.R;
import com.nitish.githubapi.beans.RepositoryApiResponse;

import java.util.List;

public class RepositorysRecyclerViewAdapter extends RecyclerView.Adapter<RepositorysRecyclerViewAdapter.ViewHolder> {

  private List<RepositoryApiResponse> response;
  Context context;
  LayoutInflater mInflater;

  public RepositorysRecyclerViewAdapter(Context context, List<RepositoryApiResponse> response) {
    this.context=context;
    this.response=response;
    this.mInflater=LayoutInflater.from(context);

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
  }

  @Override
  public int getItemCount() {
    return response.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    View myView;
    ImageView avatar;
    TextView name;
    TextView fullName;
    TextView watcher;
    TextView commits;

    ViewHolder(View v) {
      super(v);
      avatar=v.findViewById(R.id.avatar);
      name=v.findViewById(R.id.project_name);
      fullName=v.findViewById(R.id.project_full_name);
      watcher=v.findViewById(R.id.watcher_count);
      commits=v.findViewById(R.id.commit_count);

    }
  }

}
