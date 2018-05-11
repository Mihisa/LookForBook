package com.mihisa.lookforbook;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SearchTask extends AsyncTask<String, Void, List<Volume>> {

    private SearchListener searchListener;

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        searchListener.onSearching();
    }

    @Override
    protected void onPostExecute(List<Volume> volumes) {
        super.onPostExecute(volumes);
        searchListener.onResult(volumes == null ? Collections.<Volume>emptyList() : volumes);
    }

    @Override
    protected List<Volume> doInBackground(String... strings) {
        String query = strings[0];

        Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
                .setApplicationName(BuildConfig.APPLICATION_ID)
                .build();

        try {
            Books.Volumes.List list = books.volumes().list(query).setProjection("LITE");
            return list.execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public interface SearchListener {
        void onSearching();

        void onResult(List<Volume> volumes);
    }
}
