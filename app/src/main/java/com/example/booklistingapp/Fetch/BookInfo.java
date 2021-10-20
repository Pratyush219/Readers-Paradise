package com.example.booklistingapp.Fetch;

import androidx.annotation.NonNull;

public class BookInfo {
    protected final String name;
    protected final String bookUrl;
    protected final String thumbnailUrl;
    protected final String languageCode;
    protected final String authors;
    protected boolean isFavourite;

    public String getLanguageCode() {
        return languageCode;
    }

    public BookInfo(String name, String bookUrl, String thumbnailUrl, String languageCode, String authors) {
        this.name = name;
        this.bookUrl = bookUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.languageCode = languageCode;
        this.authors = authors;
        isFavourite = false;
    }

    public String getName() {
        return name;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getAuthors() {
        return authors;
    }

    @NonNull
    @Override
    public String toString() {
        return "BookInfo{" +
                "name='" + name + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", authors=" + authors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookInfo)) return false;
        BookInfo bookInfo = (BookInfo) o;
        return getBookUrl().equals(bookInfo.getBookUrl());
    }

    @Override
    public int hashCode() {
        return getBookUrl().hashCode();
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
