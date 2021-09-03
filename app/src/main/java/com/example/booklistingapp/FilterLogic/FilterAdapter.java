package com.example.booklistingapp.FilterLogic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklistingapp.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    private List<String> filters;
    FilterViewModel model;
    private boolean flag = false;

    public FilterAdapter(ViewModelStoreOwner owner) {
        model = new ViewModelProvider(owner).get(FilterViewModel.class);
    }

    public void setFilters(List<String> filterList) {
        filters = filterList;
        if(flag) {
            model.fetchBooks();
            flag = false;
        }
        notifyDataSetChanged();
    }


    public List<String> getFilters() {
        return filters;
    }

    public void removeFilter(String filter) {
        filters.remove(filter);
        model.removeFilter(filter);
        setFilters(filters);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_layout, parent, false);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.setMarginStart(8);
        params.setMarginEnd(8);

        view.setLayoutParams(params);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.MyViewHolder holder, int position) {
        if (filters != null) {
            holder.chip.setVisibility(View.VISIBLE);
            holder.chip.setText(filters.get(position));
            holder.chip.setChecked(true);
            holder.chip.setOnClickListener(v -> {
                flag = true;
                removeFilter(((CompoundButton)v).getText().toString());
            });
        }
    }

    @Override
    public int getItemCount() {
        return (filters == null) ? 0 : filters.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = (Chip) itemView;
        }
    }

}
