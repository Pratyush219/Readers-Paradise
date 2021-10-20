package com.example.booklistingapp.favourites;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.booklistingapp.Fetch.BookInfo;

@Entity(tableName = "Favourites")
public class FavouriteItem extends BookInfo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public FavouriteItem(String name, String bookUrl, String thumbnailUrl, String languageCode, String authors) {
        super(name, bookUrl, thumbnailUrl, languageCode, authors);
    }

    public FavouriteItem(BookInfo bookInfo){
        super(bookInfo.getName(), bookInfo.getBookUrl(), bookInfo.getThumbnailUrl(), bookInfo.getLanguageCode(), bookInfo.getAuthors());
        this.isFavourite = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavouriteItem)) return false;
        if (!super.equals(o)) return false;
        FavouriteItem that = (FavouriteItem) o;
        return getBookUrl().equals(that.getBookUrl());
    }

    @Override
    public int hashCode() {
        return getBookUrl().hashCode();
    }

    public int getId() {
        return id;
    }
    public void setId(int newId){
        id = newId;
    }

}
