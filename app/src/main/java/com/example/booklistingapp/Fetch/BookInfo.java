package com.example.booklistingapp.Fetch;

public class BookInfo {
    private final String name;
    private final String bookUrl;
    private final String thumbnailUrl;
    private final String languageCode;
    private final StringBuilder authors;

    public String getLanguageCode() {
        return languageCode;
    }

    public BookInfo(String name, String bookUrl, String thumbnailUrl, String languageCode, StringBuilder authors) {
        this.name = name;
        this.bookUrl = bookUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.languageCode = languageCode;
        this.authors = authors;
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

    public StringBuilder getAuthors() {
        return authors;
    }
}
