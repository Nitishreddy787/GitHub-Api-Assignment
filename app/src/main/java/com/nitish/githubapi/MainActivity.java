package com.nitish.githubapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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
  List<ItemsItem> repositoryApiList=new ArrayList<>();
  int maxSize=10;
  ImageView filter;
  BottomSheetDialog filterDialog;
  List<String> filterData=new ArrayList<>();
  int selectedItemPos=0;
  String selectedItem="Watcher Count Descending";
  ProgressBar pBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView=findViewById(R.id.recycler_view);
    searchRepositories=findViewById(R.id.search_repositories);
    filter=findViewById(R.id.filter);
    pBar=findViewById(R.id.pBar);

    retrofit=MyRetrofit.getInstance();
    repos=retrofit.getApiRepos();

    filterData.add("Watcher Count Descending");
    filterData.add("Watcher Count Ascending");
    filterData.add("Updated Recently");
    filterData.add("Created Recently");

    filter.setOnClickListener(view->{

      if(repositoryApiList.size() > 0){
        View contentView = getLayoutInflater().inflate(R.layout.home_filter, null);

        filterDialog = new BottomSheetDialog(MainActivity.this);
        filterDialog.setContentView(contentView);

        Spinner filters = contentView.findViewById(R.id.filters);
        Button proceed=contentView.findViewById(R.id.proceed);



        ArrayAdapter<String> channelArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_view, filterData);
        channelArrayAdapter.setDropDownViewResource(R.layout.spinner_list_view);
        filters.setAdapter(channelArrayAdapter);

        for (int i=0;i<filterData.size();i++){
          if(selectedItem.equals(filterData.get(i))){
            selectedItemPos=i;
          }
        }

        filters.setSelection(selectedItemPos);

        filters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedItemPos=position;
            selectedItem=filters.getSelectedItem().toString();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
        });

        proceed.setOnClickListener(v->{
          filterDialog.dismiss();
          setRepositoryList(selectedItemPos);
        });

        filterDialog.show();
      }else{

        Toast.makeText(MainActivity.this,"There are no repository's to apply filter. \n Please search repository's then apply filter",Toast.LENGTH_SHORT).show();

      }


     });

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

    pBar.setVisibility(View.VISIBLE);

    repos.getSearchResult(query).enqueue(new Callback<SearchApiResponse>() {
      @Override
      public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
        pBar.setVisibility(View.GONE);

        if (response.body() != null) {

          Collections.sort(response.body().getItems(), (o1, o2) -> Integer.compare(o2.getWatchersCount(), o1.getWatchersCount()));

          if (response.body().getItems().size() < 10) {
            maxSize = response.body().getItems().size();
          }

          repositoryApiList=new ArrayList<>();
          for(int i=0;i<maxSize;i++) repositoryApiList.add(response.body().getItems().get(i));

          LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL,false);
          recyclerView.setLayoutManager(linearLayoutManager );
          searchRepositoryRecyclerViewAdapter =new SearchRepositoryRecyclerViewAdapter(MainActivity.this);
          recyclerView.setAdapter(searchRepositoryRecyclerViewAdapter);

          setRepositoryList(0);

        }
      }

      @Override
      public void onFailure(Call<SearchApiResponse> call, Throwable t) {
        pBar.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this,"SomeThing went wrong",Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void setRepositoryList(int position){

    switch (position){

      case 0:
        Collections.sort(repositoryApiList, (o1, o2) -> Integer.compare(o2.getWatchersCount(), o1.getWatchersCount()));
        break;

      case 1:
        Collections.sort(repositoryApiList, (o1, o2) -> Integer.compare(o1.getWatchersCount(), o2.getWatchersCount()));
        break;

      case 2:
        Collections.sort(repositoryApiList, (o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        break;

      case 3:
        Collections.sort(repositoryApiList, (o1, o2) -> o2.getCreatedAt().compareTo(o1.getUpdatedAt()));
        break;

      default:
        Collections.sort(repositoryApiList, (o1, o2) -> Integer.compare(o2.getWatchersCount(), o1.getWatchersCount()));
        break;
    }

    searchRepositoryRecyclerViewAdapter.setRepositoryList(repositoryApiList);

  }

}
