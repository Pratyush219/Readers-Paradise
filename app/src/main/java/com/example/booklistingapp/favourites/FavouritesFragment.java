package com.example.booklistingapp.favourites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklistingapp.Fetch.BookInfo;
import com.example.booklistingapp.Fetch.MyAdapter;
import com.example.booklistingapp.MainActivity;
import com.example.booklistingapp.R;

import java.util.List;
import java.util.Objects;

public class FavouritesFragment extends Fragment implements MyAdapter.OnBookListener {
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("FragmentCreated", "FavouritesFragment");

        navController = Navigation.findNavController(view);
        RecyclerView recyclerView = view.findViewById(R.id.favourites_list);
        setHasOptionsMenu(true);

        TextView empty = view.findViewById(R.id.empty_favourites);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Your Favourite Books");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FavouriteAdapter adapter = new FavouriteAdapter(this, requireActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MainActivity.favouritesViewModel.fetchAll().observe(getViewLifecycleOwner(), new Observer<List<FavouriteItem>>() {
            @Override
            public void onChanged(List<FavouriteItem> favouriteItems) {
                if(favouriteItems.size() > 0){
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else{
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                Log.i("NumLiveData", favouriteItems.size() + "");
                adapter.setFavourites(favouriteItems);
            }
        });
    }

    @Override
    public void onBookClick(BookInfo bookInfo) {
        String bookUrl = bookInfo.getBookUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookUrl));
        if(intent.resolveActivity(requireActivity().getPackageManager()) != null){
            requireActivity().startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.clear_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            navController.popBackStack();
        }
        else{
            MainActivity.favouritesViewModel.deleteAll();
            Toast.makeText(getContext(), "Favourites list cleared", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}