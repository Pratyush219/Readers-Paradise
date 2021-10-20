package com.example.booklistingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class LandingFragment extends Fragment {

    Button startButton;
    NavController navController;
    public LandingFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startButton = view.findViewById(R.id.begin);
//        ImageButton btn = view.findViewById(R.id.up_button);
//        btn.setVisibility(View.GONE);
        navController = Navigation.findNavController(view);
        startButton.setOnClickListener(onClick -> navController.navigate(R.id.action_landingFragment_to_booksFragment));

    }
}