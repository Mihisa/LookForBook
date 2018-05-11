package com.mihisa.lookforbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.api.services.books.model.Volume;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Volume volume;
    private int spanCount;

    public BookViewHolder(ViewGroup viewGroup) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book, viewGroup, false));
        itemView.setOnClickListener(this);
    }

    public void setVolume(Volume volume) {
        this.volume = volume;

        System.out.println(volume.getVolumeInfo().getInfoLink());

        int approximateWidth = 300;
        int approximateHeight = 400;

        DisplayMetrics displayMetrics = itemView.getContext().getResources().getDisplayMetrics();

        int screenWidth = displayMetrics.widthPixels;

        int width = screenWidth / spanCount;
        int height = (approximateHeight * width) / approximateWidth;

        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.width = width;
        params.height = height;
        itemView.setLayoutParams(params);
        itemView.invalidate();

        Volume.VolumeInfo.ImageLinks imageLinks = volume.getVolumeInfo().getImageLinks();

        if (imageLinks != null) {
            String medium = imageLinks.getMedium();
            String large = imageLinks.getLarge();
            String small = imageLinks.getSmall();
            String thumbnail = imageLinks.getThumbnail();
            String smallThumbnail = imageLinks.getSmallThumbnail();

            String imageLink = "";
            if (large != null) {
                imageLink = large;
            } else if (medium != null) {
                imageLink = medium;
            } else if (small != null) {
                imageLink = small;
            } else if (thumbnail != null) {
                imageLink = thumbnail;
            } else if (smallThumbnail != null) {
                imageLink = smallThumbnail;
            }

            //TODO не скругленные углы
            imageLink = imageLink.replace("edge=curl", "");
            System.out.println(imageLink);

            Glide.with(itemView.getContext())
                    .load(imageLink)
                    .into((ImageView) itemView);
        } else {
            System.err.println("No images found");
        }
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public void onClick(View v) {

        Bundle data = new Bundle();

        Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
        Volume.SaleInfo saleInfo = volume.getSaleInfo();

        if (volumeInfo != null) {
            Volume.VolumeInfo.ImageLinks imageLinks = volumeInfo.getImageLinks();
            if (volumeInfo.getTitle() != null)
                data.putString(BookData.TITLE, volumeInfo.getTitle());
            if (volumeInfo.getSubtitle() != null)
                data.putString(BookData.SUBTITLE, volumeInfo.getSubtitle());
            if (volumeInfo.getDescription() != null)
                data.putString(BookData.DESCRIPTION, volumeInfo.getDescription());
            if (volumeInfo.getPublisher() != null)
                data.putString(BookData.PUBLISHER, volumeInfo.getPublisher());
            if (volumeInfo.getAuthors() != null)
                data.putStringArray(BookData.AUTHORS, volumeInfo.getAuthors().toArray(new String[volumeInfo.getAuthors().size()]));
            if (volumeInfo.getPublishedDate() != null)
                data.putString(BookData.PUBLISHED_DATE, volumeInfo.getPublishedDate());
            if (saleInfo != null) {
                Volume.SaleInfo.RetailPrice retailPrice = saleInfo.getRetailPrice();
                Volume.SaleInfo.ListPrice listPrice = saleInfo.getListPrice();
                if (retailPrice != null) {
                    data.putDouble(BookData.RETAIL_PRICE, retailPrice.getAmount());
                    data.putString(BookData.RETAIL_PRICE_CURRENCY_CODE, retailPrice.getCurrencyCode());
                }
                if (listPrice != null) {
                    data.putDouble(BookData.LIST_PRICE, listPrice.getAmount());
                    data.putString(BookData.LIST_PRICE_CURRENCY_CODE, listPrice.getCurrencyCode());
                }
            }
            if (imageLinks != null) {
                String image = null;
                if (imageLinks.getExtraLarge() != null) {
                    image = imageLinks.getExtraLarge();
                } else if (imageLinks.getLarge() != null) {
                    image = imageLinks.getLarge();
                } else if (imageLinks.getMedium() != null) {
                    image = imageLinks.getMedium();
                } else if (imageLinks.getSmall() != null) {
                    image = imageLinks.getSmall();
                } else if (imageLinks.getThumbnail() != null) {
                    image = imageLinks.getThumbnail();
                } else if (imageLinks.getSmallThumbnail() != null) {
                    image = imageLinks.getSmallThumbnail();
                }
                if (image != null) {
                    data.putString(BookData.IMAGE, image);
                }
            }
        }
        Context context = itemView.getContext();
        context.startActivity(new Intent(context, BookInfoActivity.class).putExtra("data", data));
    }
}
