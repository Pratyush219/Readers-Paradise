package com.example.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklistingapp.Fetch.MyAdapter;
import com.example.booklistingapp.FilterLogic.FilterAdapter;
import com.example.booklistingapp.FilterLogic.FilterViewModel;

import java.util.ArrayList;
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

        model = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        model.init();
        //Set up RecyclerView
        RecyclerView appliedFilters = view.findViewById(R.id.applied_filters);
        RecyclerView bookList = view.findViewById(R.id.book_list);

        FilterAdapter adapter = new FilterAdapter(requireActivity());
        MyAdapter myAdapter = new MyAdapter(this);

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
        if(network == null || !network.isConnectedOrConnecting()){
            errorView.setVisibility(View.VISIBLE);
            introView.setVisibility(View.GONE);
            bookList.setVisibility(View.GONE);
        }

        liveData = new ConnectionLiveData(requireContext());

        liveData.observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                errorView.setVisibility(View.GONE);
                if(bookList.getChildCount() == 0){
                    bookList.setVisibility(View.GONE);
                    introView.setVisibility(View.VISIBLE);
                }
                else{
                    introView.setVisibility(View.GONE);
                    bookList.setVisibility(View.VISIBLE);
                }
            }
            else{
                bookList.setVisibility(View.GONE);
                introView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
            }
        });
        //Observe filterList LiveData and update FilterRecyclerView
        model.getFilters().observe(getViewLifecycleOwner(), strings -> {
            ArrayList<String> filters = new ArrayList<>(strings);
            adapter.setFilters(filters);
        });
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
            if(infos.size() > 0){
                bookList.setVisibility(View.VISIBLE);
                introView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                myAdapter.setBooks(infos);
            }
            else{
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

        ImageButton upButton = view.findViewById(R.id.up_button);
        upButton.setOnClickListener(v -> requireActivity().onBackPressed());

    }

    @Override
    public void onBookClick(int position) {
        String bookUrl = Objects.requireNonNull(model.getBookInfoList().getValue()).get(position).getBookUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookUrl));
        if(intent.resolveActivity(requireActivity().getPackageManager()) != null){
            requireActivity().startActivity(intent);
        }
    }
}