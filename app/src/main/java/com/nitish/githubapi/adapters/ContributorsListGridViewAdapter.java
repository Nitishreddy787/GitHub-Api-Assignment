package com.nitish.githubapi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.nitish.githubapi.R;
import com.nitish.githubapi.beans.ContributorsApiResponse;
import com.nitish.githubapi.beans.RepositoryApiResponse;
import com.nitish.githubapi.beans.UserProfileApiResponse;
import com.nitish.githubapi.services.MyRetrofit;
import com.nitish.githubapi.services.Repos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContributorsListGridViewAdapter extends ArrayAdapter<ContributorsApiResponse> {

    private Context context;
    private List<ContributorsApiResponse> response;
    LayoutInflater mInflater;
    MyRetrofit retrofit;
    Repos repos;

    public ContributorsListGridViewAdapter(@NonNull Context context, List<ContributorsApiResponse> response) {
        super(context, 0);
        this.context=context;
        this.response=response;
        this.mInflater=LayoutInflater.from(context);

        retrofit=MyRetrofit.getInstance();
        repos=retrofit.getApiRepos();

    }

    @Override
    public int getCount() {
        return response.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.contributors_grid_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(String.valueOf(response.get(position).getId()));
        Glide.with(context).load(response.get(position).getAvatarUrl()).into(holder.avatar);

        contributorFullDetails(position,holder.name);

        return convertView;
    }

    private static class ViewHolder {

        TextView name;
        de.hdodenhof.circleimageview.CircleImageView avatar;

        ViewHolder(View view) {
            name=view.findViewById(R.id.name);
            avatar=view.findViewById(R.id.avatar);
        }

    }

    private void contributorFullDetails(int position, TextView name){

        repos.userProfile(response.get(position).getLogin()).enqueue(new Callback<UserProfileApiResponse>() {
            @Override
            public void onResponse(Call<UserProfileApiResponse> call, Response<UserProfileApiResponse> response) {
                ((Activity) context).runOnUiThread(()->{
                    if(response.body() != null){
                        name.setText(String.valueOf(response.body().getName()));
                    }
                });
            }

            @Override
            public void onFailure(Call<UserProfileApiResponse> call, Throwable t) {
                ((Activity) context).runOnUiThread(()->{

                });
            }
        });
    }

}
