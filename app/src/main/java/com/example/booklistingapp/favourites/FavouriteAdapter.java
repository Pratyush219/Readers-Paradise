package com.example.booklistingapp.favourites;

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

import com.example.booklistingapp.Fetch.MyAdapter;
import com.example.booklistingapp.MainActivity;
import com.example.booklistingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    protected List<FavouriteItem> favourites = new ArrayList<>();
    private final MyAdapter.OnBookListener onBookListener;
    private FavouriteButtonClickListener favouriteListener;
    private final LifecycleOwner owner;

    public FavouriteAdapter(MyAdapter.OnBookListener bookListener, LifecycleOwner owner) {
        onBookListener = bookListener;
        this.owner = owner;
    }

    public void setFavourites(List<FavouriteItem> favourites) {
        this.favourites = favourites;
        Log.i("NumFavs", this.favourites.size() + "");
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Log.i("EnteredIn", "onCreateViewHolder");
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Log.i("EnteredIn", "onBindViewHolder");
        if(favourites != null) Log.i("NumFavs", favourites.size() + "");
        String heading = null;
        if (favourites != null) {
            heading = favourites.get(position).getName();
        }
        holder.title.setText(heading);
        String authorNames = favourites.get(position).getAuthors();
        holder.author.setText(authorNames);
        String thumbnailUrl = favourites.get(position).getThumbnailUrl();
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
        holder.favourite.setVisibility(View.VISIBLE);
        holder.notFavourite.setVisibility(View.GONE);

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.favouritesViewModel.delete(favourites.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView thumbnail;
        private final TextView title;
        private final TextView author;
        private final ImageView favourite;
        private final ImageView notFavourite;
        public FavouriteViewHolder(@NonNull View itemView){
            super(itemView);
            Log.i("EnteredIn", "FavouriteViewHolder");
            ConstraintLayout layout = itemView.findViewById(R.id.book_info);
            thumbnail = itemView.findViewById(R.id.bookImage);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authors);
            favourite = itemView.findViewById(R.id.favourite);
            notFavourite = itemView.findViewById(R.id.not_favourite);
            Log.i("EnteredIn", "MyViewHolder");

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBookListener.onBookClick(favourites.get(getAbsoluteAdapterPosition()));
        }
    }
}
