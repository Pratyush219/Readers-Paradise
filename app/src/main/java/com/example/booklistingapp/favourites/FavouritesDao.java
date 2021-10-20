package com.example.booklistingapp.favourites;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.booklistingapp.Fetch.BookInfo;

import java.util.List;

@Dao
interface FavouritesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavouriteItem item);

    @Delete
    void delete(FavouriteItem item);

    @Update
    void update(FavouriteItem item);

    @Query("SELECT * FROM Favourites")
    LiveData<List<FavouriteItem>> fetchAll();

    @Query("SELECT * FROM Favourites WHERE id LIKE :id")
    LiveData<FavouriteItem> fetchById(int id);

    @Query("SELECT COUNT(name) FROM Favourites WHERE bookUrl LIKE :url")
    LiveData<Integer> numUser(String url);

    @Query("DELETE FROM Favourites WHERE bookUrl LIKE :url")
    void deleteIfUrlPresent(String url);

    @Query("DELETE FROM Favourites")
    void deleteAll();

    @Query("SELECT COUNT(name) FROM Favourites")
    int getRowCount();
}
