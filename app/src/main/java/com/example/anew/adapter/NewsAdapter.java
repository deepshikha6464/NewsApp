package com.example.anew.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.anew.R;
import com.example.anew.model.NewsModel;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter< NewsAdapter.NewsViewHolder> {
    private static final String TAG = "NewsAdapter";
    List<NewsModel> newsList ;
Context context;
    private ItemClickListener mClickListener;
    public NewsAdapter( List<NewsModel> newsList) {
        this.newsList = newsList;

    }

    public NewsAdapter(List<NewsModel> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
       // this.mClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flip_animation, parent, false);
        return new NewsViewHolder(view,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.headline.setText(news.getTitle());
        Glide.with(context)
                .load(news.getUrlToImage())
                .into(holder.image);
        holder.content.setText(news.getDescription());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        //ui
        ImageView image;
        TextView headline,content;
        LottieAnimationView load, save, share;
        ItemClickListener itemClickListener;
        EasyFlipView myEasyFlipView;

        public NewsViewHolder(@NonNull View itemView, ItemClickListener mClickListener) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            headline = itemView.findViewById(R.id.headline);
            load = itemView.findViewById(R.id.view);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);
            content = itemView.findViewById(R.id.content);
            itemClickListener = mClickListener;
            myEasyFlipView = itemView.findViewById(R.id.myEasyFlipView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myEasyFlipView.flipTheView();
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }

        }



    }

    NewsModel getItem(int id) {
        return newsList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
