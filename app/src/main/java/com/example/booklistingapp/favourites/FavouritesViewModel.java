package com.example.booklistingapp.favourites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {
    private LiveData<List<FavouriteItem>> allFavourites;
    private FavouritesRepo repo;
    private boolean launched = false;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if(allFavourites == null){
            repo = new FavouritesRepo(getApplication());
            allFavourites = repo.fetchAll();
        }
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public void insert(FavouriteItem item){
        repo.insert(item);
        FavouriteBooksReference.favouriteBooksUrls.add(item.getBookUrl());
    }
    public void delete(FavouriteItem item){
        repo.delete(item);
        FavouriteBooksReference.favouriteBooksUrls.remove(item.getBookUrl());
    }
    public void update(FavouriteItem item){
        repo.update(item);
    }
    public LiveData<List<FavouriteItem>> fetchAll(){
        return allFavourites;
    }
    public LiveData<Integer> numUsers(String url){
        return repo.numUsers(url);
    }
    public void deleteAll(){
        FavouritesDatabase.service.execute(repo::deleteAll);
        FavouriteBooksReference.favouriteBooksUrls.clear();
    }
    public void deleteIfUrlPresent(String url){
        FavouritesDatabase.service.execute(() -> repo.deleteIfUrlPresent(url));
        FavouriteBooksReference.favouriteBooksUrls.remove(url);
    }
    public void getRowCount(){
        repo.getRowCount();
    }
    public List<FavouriteItem> getAllFavourites(){
        return allFavourites.getValue();
    }
}
