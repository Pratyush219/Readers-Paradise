package com.example.booklistingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.booklistingapp.FilterLogic.FilterViewModel;
import com.example.booklistingapp.favourites.FavouritesViewModel;

public class MainActivity extends AppCompatActivity {
    public static FavouritesViewModel favouritesViewModel;
    FilterViewModel viewModel;
    public static boolean launched = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(FavouritesViewModel.class);
        favouritesViewModel.init();
//        favouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(FavouritesViewModel.class);
    }
}