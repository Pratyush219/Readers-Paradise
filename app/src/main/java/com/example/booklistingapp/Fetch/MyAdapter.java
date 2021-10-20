package com.example.booklistingapp.Fetch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklistingapp.R;
import com.example.booklistingapp.favourites.FavouriteBooksReference;
import com.example.booklistingapp.favourites.FavouriteButtonClickListener;
import com.example.booklistingapp.favourites.FavouriteItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    protected ArrayList<BookInfo> infos;
    protected List<FavouriteItem> favourites = new ArrayList<>();
    private final OnBookListener onBookListener;
    private boolean isFavouriteList = false;
    private LifecycleOwner owner;

    public MyAdapter(OnBookListener bookListener, LifecycleOwner owner) {
        onBookListener = bookListener;
        this.owner = owner;
    }

    public void setBooks(List<BookInfo> bookList){
        isFavouriteList = false;
        infos = new ArrayList<>(bookList);
        notifyDataSetChanged();
    }

    public void setFavourites(List<FavouriteItem> favourites) {
        isFavouriteList = true;
        this.favourites = favourites;
        Log.i("NumFavs", this.favourites.size() + "");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        Log.i("EnteredIn", "onCreateViewHolder");
        return new MyViewHolder(view, onBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("EnteredIn", "onBindViewHolder");
        if(favourites != null)
            Log.i("NumFavs", favourites.size() + "");
        String heading;
        if (isFavouriteList) {
            heading = favourites.get(position).getName();
        }
        else{
            heading = infos.get(position).getName();
        }
        holder.title.setText(heading);

        String authorNames;
        if (isFavouriteList) {
            authorNames = favourites.get(position).getName();
        }
        else{
            authorNames = infos.get(position).getName();
        }
        holder.author.setText(authorNames);

        String thumbnailUrl;
        if (isFavouriteList) {
            thumbnailUrl = favourites.get(position).getThumbnailUrl();
        }
        else{
            thumbnailUrl = infos.get(position).getThumbnailUrl();
        }
        Log.i("ItemDetails", heading + authorNames);

        if(!thumbnailUrl.contains("https")){
            thumbnailUrl = thumbnailUrl.replace("http","https");
        }
        if (!thumbnailUrl.equals("")){
            Picasso.get()
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.book)
                    .error(R.drawable.book)
                    .resize(104,132)
                    .into(holder.thumbnail);
        }

        if (FavouriteBooksReference.favouriteBooksUrls.contains(infos.get(position).getBookUrl())) {
            infos.get(position).setFavourite(true);
        } else {
            infos.get(position).setFavourite(false);
        }
        if(isFavouriteList || infos.get(position).isFavourite()){
            holder.favourite.setVisibility(View.VISIBLE);
            holder.notFavourite.setVisibility(View.GONE);
        }
        else{
            holder.favourite.setVisibility(View.GONE);
            holder.notFavourite.setVisibility(View.VISIBLE);
        }
        holder.favourite.setOnClickListener(new FavouriteButtonClickListener(holder.favourite, holder.notFavourite, infos.get(position), owner));
        holder.notFavourite.setOnClickListener(new FavouriteButtonClickListener(holder.favourite, holder.notFavourite, infos.get(position), owner));
    }

    @Override
    public int getItemCount() {
        return (isFavouriteList ? favourites.size() : infos.size());
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ConstraintLayout layout;
        private ImageView thumbnail;
        private TextView title, author;
        private OnBookListener listener;
        private ImageView favourite, notFavourite;
        public MyViewHolder(View itemView, OnBookListener bookListener) {
            super(itemView);
            layout = itemView.findViewById(R.id.book_info);
            thumbnail = itemView.findViewById(R.id.bookImage);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authors);
            favourite = itemView.findViewById(R.id.favourite);
            notFavourite = itemView.findViewById(R.id.not_favourite);
            Log.i("EnteredIn", "MyViewHolder");

            listener = bookListener;

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onBookClick(infos.get(getAbsoluteAdapterPosition()));
        }
    }

    @FunctionalInterface
    public interface OnBookListener{
        void onBookClick(BookInfo bookInfo);
    }
}
