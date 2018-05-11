package com.mihisa.lookforbook;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchTask.SearchListener {

    private boolean isSearch;

    private SearchTask searchTask;
    private SearchTask.SearchListener searchListener;

    private String lastQuery;
    private List<Volume> volumeList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        searchListener = (SearchTask.SearchListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //сохраняем данные
        setRetainInstance(true);
    }

    public void searchBooks(String query) {
        if (query.equalsIgnoreCase(lastQuery)) return;
        //завершаем предыдущую задачу
        if (searchTask != null) searchTask.cancel(true);
        lastQuery = query;
        searchTask = new SearchTask();
        searchTask.setSearchListener(this);
        searchTask.execute(query);
    }

    @Override
    public void onSearching() {
        isSearch = true;
        volumeList.clear();
        searchListener.onSearching();
    }

    @Override
    public void onResult(List<Volume> volumes) {
        isSearch = false;
        volumeList = volumes;
        searchListener.onResult(volumeList);
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public List<Volume> getVolumeList() {
        return volumeList;
    }
}
