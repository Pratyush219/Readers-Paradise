package com.example.booklistingapp.Fetch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklistingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<BookInfo> infos;
    private final OnBookListener onBookListener;

    public MyAdapter(OnBookListener bookListener) {
        onBookListener = bookListener;
    }

    public void setBooks(List<BookInfo> bookList){
        infos = new ArrayList<>(bookList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view, onBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String heading = infos.get(position).getName();
        holder.title.setText(heading);
        StringBuilder authorNames = infos.get(position).getAuthors();
        holder.author.setText(authorNames);
        String thumbnailUrl = infos.get(position).getThumbnailUrl();
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
    }

    @Override
    public int getItemCount() {
        if(infos == null) return 0;
        return infos.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout layout;
        ImageView thumbnail;
        TextView title, author;
        OnBookListener listener;
        public MyViewHolder(View itemView, OnBookListener bookListener) {
            super(itemView);
            layout = itemView.findViewById(R.id.book_info);
            thumbnail = itemView.findViewById(R.id.bookImage);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.price);
            listener = bookListener;

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onBookClick(getAbsoluteAdapterPosition());
        }
    }

    @FunctionalInterface
    public interface OnBookListener{
        void onBookClick(int position);
    }
}
