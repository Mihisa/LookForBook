package com.mihisa.lookforbook;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mihisa.lookforbook.databinding.ActivityBookInfoBinding;

public class BookInfoActivity extends AppCompatActivity {

    Bundle data;
    ActivityBookInfoBinding bookInfoBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_info);
        Toolbar toolbar = bookInfoBinding.toolbar;
        setSupportActionBar(toolbar);
        bookInfoBinding.toolbarLayout.setTitleEnabled(false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getBundleExtra("data");
        updateView();
    }

    private void updateView() {

        Glide.with(this).load(data.getString(BookData.IMAGE)).into(bookInfoBinding.cover);

        String unknown = getString(R.string.unknown);

        bookInfoBinding.bookTitle.setText(data.containsKey(BookData.TITLE) ? data.getString(BookData.TITLE) : unknown);
        bookInfoBinding.contentBookInfo.publishedDate.append(data.containsKey(BookData.PUBLISHED_DATE) ? data.getString(BookData.PUBLISHED_DATE) : unknown);
        bookInfoBinding.contentBookInfo.publisher.append(data.containsKey(BookData.PUBLISHER) ? data.getString(BookData.PUBLISHER) : unknown);
        if (data.containsKey(BookData.AUTHORS)) {
            String author = "\n";
            String[] authors = data.getStringArray(BookData.AUTHORS);
            for (int i = 0; i < authors.length; i++) {
                String singleAuthor = authors[i];
                if (TextUtils.isEmpty(singleAuthor)) {
                    continue;
                }
                author += singleAuthor.concat(i == authors.length - 1 ? "" : "\n");
                bookInfoBinding.contentBookInfo.author.append(author);
            }
        }
        if (data.containsKey(BookData.SUBTITLE)) {
            bookInfoBinding.subtitle.setText(data.getString(BookData.SUBTITLE));
        } else {
            bookInfoBinding.subtitle.setVisibility(View.GONE);
        }
        if (data.containsKey(BookData.DESCRIPTION)) {
            bookInfoBinding.contentBookInfo.description.setText(data.getString(BookData.DESCRIPTION));
        }
    }


}
