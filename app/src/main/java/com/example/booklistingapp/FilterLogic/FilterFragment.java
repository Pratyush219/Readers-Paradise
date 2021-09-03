package com.example.booklistingapp.FilterLogic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.booklistingapp.ListDefaults;
import com.example.booklistingapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FilterFragment extends BottomSheetDialogFragment {
    ChipGroup categoriesGroup;
    ChipGroup languagesGroup;
    FilterViewModel model;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        model.init();

        String[] categories = {
          "Action and Adventure",
          "Drama",
          "Horror",
          "Mystery",
          "Self help",
          "Romance"
        };
        ListDefaults.categories = new ArrayList<>(Arrays.asList(categories));
        String[][] languages = {
                {"English","en"},
                {"Hindi","hi"},
                {"Gujarati","gu"},
                {"Bengali","bn"},
                {"Marathi","mr"},
                {"Spanish","es"},
                {"French","fr"}
        };
        ArrayList<String> filters = (ArrayList<String>) model.getFilters().getValue();
        ListDefaults.languages = new HashMap<>();
        for(String[] lan: languages){
            (ListDefaults.languages).put(lan[0], lan[1]);
        }
        categoriesGroup = view.findViewById(R.id.categories_group);
        languagesGroup = view.findViewById(R.id.language_group);
        for (String category : categories) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_layout, categoriesGroup, false);
            chip.setText(category);
            categoriesGroup.addView(chip);
            assert filters != null;
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if(!filters.contains(category)) model.addFilter(category);
                } else {
                    if(filters.contains(category)) model.removeFilter(category);
                }
            });
        }
        for (String[] language : languages) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_layout, languagesGroup, false);
            chip.setText(language[0]);
            languagesGroup.addView(chip);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if(!filters.contains(language[0])) model.addFilter(language[0]);
                } else {
                    if(filters.contains(language[0])) model.removeFilter(language[0]);
                }
            });
            chip.setOnClickListener(v -> model.fetchBooks());
        }
        model.getFilters().observe(this, strings -> {
            for(int i = 0; i < categories.length; i++){
                ((Chip)categoriesGroup.getChildAt(i)).setChecked(strings.contains(categories[i]));
            }
            for(int i = 0; i < languages.length; i++){
                ((Chip)languagesGroup.getChildAt(i)).setChecked(strings.contains(languages[i][0]));
            }
        });
    }
}