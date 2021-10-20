package com.example.booklistingapp.favourites;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.booklistingapp.Fetch.BookInfo;
import com.example.booklistingapp.MainActivity;

import java.util.List;
import java.util.Objects;

public class FavouriteButtonClickListener implements View.OnClickListener {
    private ImageView favourite;
    private ImageView notFavourite;
    private BookInfo bookInfo;
    private LifecycleOwner owner;

    public FavouriteButtonClickListener(ImageView favourite, ImageView notFavourite, BookInfo bookInfo, LifecycleOwner owner) {
        this.favourite = favourite;
        this.notFavourite = notFavourite;
        this.bookInfo = bookInfo;
        this.owner = owner;
    }

    @Override
    public void onClick(View view) {
        Log.i("ClickListnerOf", "Favourite Button");
        if(favourite.getVisibility() == View.VISIBLE){
            favourite.setVisibility(View.GONE);
            notFavourite.setVisibility(View.VISIBLE);
            MainActivity.favouritesViewModel.deleteIfUrlPresent(bookInfo.getBookUrl());
            bookInfo.setFavourite(false);

            Toast.makeText(view.getContext(), bookInfo.toString() + "deleted", Toast.LENGTH_SHORT).show();
        }
        else{
            notFavourite.setVisibility(View.GONE);
            favourite.setVisibility(View.VISIBLE);
            bookInfo.setFavourite(true);
            MainActivity.favouritesViewModel.insert(new FavouriteItem(bookInfo));
        }

    }
}
