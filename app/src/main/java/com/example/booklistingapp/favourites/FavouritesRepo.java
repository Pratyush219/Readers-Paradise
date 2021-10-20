package com.example.booklistingapp.favourites;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.booklistingapp.Fetch.BookInfo;

import java.util.List;

public class FavouritesRepo {
    private LiveData<List<FavouriteItem>> favourites;
    private final FavouritesDao favouritesDao;

    public FavouritesRepo(Context context) {
        favouritesDao = FavouritesDatabase.getInstance(context).favouritesDao();
        favourites = favouritesDao.fetchAll();
    }

    public void insert(FavouriteItem item){
        FavouritesDatabase.service.execute(() -> {
            favouritesDao.insert(item);
            getRowCount();
        });
    }
    public void delete(FavouriteItem item){
        FavouritesDatabase.service.execute(() -> {
            favouritesDao.delete(item);
            getRowCount();
        });
    }
    public void update(FavouriteItem item){
        FavouritesDatabase.service.execute(() -> favouritesDao.update(item));
    }
    public LiveData<List<FavouriteItem>> fetchAll(){
        return favourites;
    }
    public LiveData<Integer> numUsers(String url){
        return favouritesDao.numUser(url);
    }
    public void deleteAll(){
        FavouritesDatabase.service.execute(() -> {
            favouritesDao.deleteAll();
            getRowCount();
        });
    }
    public void deleteIfUrlPresent(String url){
        FavouritesDatabase.service.execute(() -> {
            favouritesDao.deleteIfUrlPresent(url);
        });
    }

    public void getRowCount(){
        FavouritesDatabase.service.execute(() -> Log.i("NumFavourites", String.valueOf(favouritesDao.getRowCount())));
    }
}
