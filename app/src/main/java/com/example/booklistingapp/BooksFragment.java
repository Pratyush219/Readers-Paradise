package com.example.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklistingapp.Fetch.BookInfo;
import com.example.booklistingapp.Fetch.MyAdapter;
import com.example.booklistingapp.FilterLogic.FilterAdapter;
import com.example.booklistingapp.FilterLogic.FilterViewModel;
import com.example.booklistingapp.favourites.FavouriteBooksReference;
import com.example.booklistingapp.favourites.FavouriteItem;

import java.util.List;
import java.util.Objects;


public class BooksFragment extends Fragment implements MyAdapter.OnBookListener {
    FilterViewModel model;
    private NavController navController;
    private EditText searchField;
    private long mLastClickTime = 0;
    private ConnectionLiveData liveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        model = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        model.init();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Books");
        //Set up RecyclerView
        RecyclerView appliedFilters = view.findViewById(R.id.applied_filters);
        RecyclerView bookList = view.findViewById(R.id.book_list);

        FilterAdapter adapter = new FilterAdapter(requireActivity());
        MyAdapter myAdapter = new MyAdapter(this, requireActivity());

        TextView introView = view.findViewById(R.id.intro);
        TextView errorView = view.findViewById(R.id.no_internet);
        TextView noResultsView = view.findViewById(R.id.no_results);

        noResultsView.setVisibility(View.GONE);
        bookList.setVisibility(View.GONE);
        introView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);

        bookList.setAdapter(myAdapter);
        bookList.setLayoutManager(new LinearLayoutManager(getContext()));

        appliedFilters.setAdapter(adapter);
        appliedFilters.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        ConnectivityManager manager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        if (network == null || !network.isConnectedOrConnecting()) {
            errorView.setVisibility(View.VISIBLE);
            introView.setVisibility(View.GONE);
            bookList.setVisibility(View.GONE);
        }

        liveData = new ConnectionLiveData(requireContext());

        if (!MainActivity.favouritesViewModel.isLaunched()) {
            MainActivity.favouritesViewModel.fetchAll().observe(requireActivity(), new Observer<List<FavouriteItem>>() {
                @Override
                public void onChanged(List<FavouriteItem> favouriteItems) {
                    for (int i = 0; i < favouriteItems.size(); i++) {
                        FavouriteBooksReference.favouriteBooksUrls.add(favouriteItems.get(i).getBookUrl());
                    }
                    MainActivity.favouritesViewModel.setLaunched(true);
//                    introView.setText("Arrived");
                }
            });
        }
        liveData.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                errorView.setVisibility(View.GONE);
                if (bookList.getChildCount() == 0) {
                    bookList.setVisibility(View.GONE);
                    introView.setVisibility(View.VISIBLE);
                } else {
                    introView.setVisibility(View.GONE);
                    bookList.setVisibility(View.VISIBLE);
                }
            } else {
                bookList.setVisibility(View.GONE);
                introView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
            }
        });
        //Observe filterList LiveData and update FilterRecyclerView
        model.getFilters().observe(getViewLifecycleOwner(), adapter::setFilters);
        ImageButton search = view.findViewById(R.id.search_button);
        searchField = view.findViewById(R.id.search_field);
        searchField.setText(model.getSearchRequest());

        search.setOnClickListener((v) -> {
                    Log.i("Searched", searchField.getText().toString());
                    if (searchField.getText() != null && searchField.getText().length() > 0) {
                        model.setSearchRequest(searchField.getText().toString());
                        model.fetchBooks();
                    } else {
                        Toast.makeText(getContext(), "Search bar is empty", Toast.LENGTH_SHORT).show();
                        introView.setVisibility(View.VISIBLE);
                    }
                }
        );
        model.getBookInfoList().observe(getViewLifecycleOwner(), infos -> {
            if (infos.size() > 0) {
                bookList.setVisibility(View.VISIBLE);
                introView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                myAdapter.setBooks(infos);
            } else {
                noResultsView.setVisibility(View.VISIBLE);
                bookList.setVisibility(View.GONE);
                introView.setVisibility(View.GONE);
            }
        });
        ImageButton filterButton = view.findViewById(R.id.filter_button);
        navController = Navigation.findNavController(view);
        filterButton.setOnClickListener(onClick -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            ListDefaults.flag = true;
            model.setFilters(adapter.getFilters());
            if (ListDefaults.flag)
                navController.navigate(R.id.action_booksFragment_to_filterFragment);
        });

    }

    @Override
    public void onBookClick(BookInfo bookInfo) {
        String bookUrl = bookInfo.getBookUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookUrl));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            requireActivity().startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.go_to_favourites_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
        } else
            navController.navigate(R.id.action_booksFragment_to_favouritesFragment);
        return super.onOptionsItemSelected(item);
    }
}