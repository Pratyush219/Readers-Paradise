package com.example.booklistingapp.Fetch;

import java.util.List;

@FunctionalInterface
public interface BooksUpdater {
    void updateBooks(List<BookInfo> updatedList);
}
