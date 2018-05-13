package com.mihisa.lookforbook;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;
import com.mihisa.lookforbook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SearchTask.SearchListener {

    private List<Volume> volumeList;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        SearchFragment searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag("searchFragment");
        if(searchFragment != null) {
            volumeList = searchFragment.getVolumeList();
            binding.searchView.setQuery(searchFragment.getLastQuery(), false);
        } else {
            volumeList = new ArrayList<>();
            searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction()
                    .add(searchFragment, "searchFragment")
                    .commit();
        }

        RecyclerView recyclerView = binding.searchResult;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        BookAdapter adapter = new BookAdapter(volumeList, gridLayoutManager.getSpanCount());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchFragment searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag("searchFragment");
                if(searchFragment != null) searchFragment.searchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onSearching() {
        volumeList.clear();
        binding.searchResult.getAdapter().notifyDataSetChanged();
        binding.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResult(List<Volume> volumes) {
        binding.loadingView.setVisibility(View.GONE);
        volumeList.addAll(volumes);
        binding.searchResult.getAdapter().notifyDataSetChanged();
    }
}
