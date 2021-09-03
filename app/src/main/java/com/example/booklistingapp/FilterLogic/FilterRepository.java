package com.example.booklistingapp.FilterLogic;

import java.util.ArrayList;
import java.util.List;

public class FilterRepository {
    private static FilterRepository instance;
    ArrayList<String> filterList = new ArrayList<>();



    public static FilterRepository getInstance(){
        if(instance == null){
            synchronized (FilterRepository.class) {
                if (instance == null) {
                    instance = new FilterRepository();
                }
            }
        }
        return instance;
    }
    public void addFilter(String filter){
        filterList.add(filter);
    }
    public void removeFilter(String filter){
        filterList.remove(filter);
    }
    public List<String> getFilterList(){
        return filterList;
    }


}
