package com.example.booklistingapp.FilterLogic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.booklistingapp.Fetch.BookInfo;
import com.example.booklistingapp.Fetch.BooksRepo;
import com.example.booklistingapp.Fetch.BooksUpdater;

import java.util.List;
import java.util.Objects;

public class FilterViewModel extends AndroidViewModel implements BooksUpdater, FilterUpdater {
    private FilterRepository repository;
    private MutableLiveData<List<String>> filters;
    private MutableLiveData<List<BookInfo>> bookInfoList;
    private String searchRequest;

    public FilterViewModel(@NonNull Application application) {
        super(application);
    }
    public void init(){
        if(filters == null){
            filters = new MutableLiveData<>();
            bookInfoList = new MutableLiveData<>();
            repository = FilterRepository.getInstance();
        }
        if(repository.getFilterList() != null) filters.setValue(repository.getFilterList());
    }

    public LiveData<List<String>> getFilters(){
        return filters;
    }

    public String getSearchRequest() {
        return searchRequest;
    }

    public void fetchBooks(){
        BooksRepo repo = BooksRepo.getInstance(getApplication());
        repo.fetchBooks(searchRequest, Objects.requireNonNull(filters.getValue()), this);
    }

    public LiveData<List<BookInfo>> getBookInfoList() {
        return bookInfoList;
    }

    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }

    public void setFilters(List<String> filterList) {
        if(filterList == null) return;
        if(Objects.requireNonNull(filters.getValue()).toString().equals(filterList.toString())) return;
        this.filters.setValue(filterList);
    }

    public void addFilter(String filter){
        repository.addFilter(filter);
        filters.setValue(repository.getFilterList());
    }
    public void removeFilter(String filter){
        repository.removeFilter(filter);
        filters.setValue(repository.getFilterList());
    }
    @Override
    public void updateBooks(List<BookInfo> updatedList) {
        bookInfoList.setValue(updatedList);
    }

    @Override
    public void updateFilter(String filterToRemove) {
        removeFilter(filterToRemove);
    }
}