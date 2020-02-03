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
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.anew.R;
import com.example.anew.model.NewsModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter< NewsAdapter.NewsViewHolder> {
    private static final String TAG = "NewsAdapter";
    List<NewsModel> newsList ;
Context context;
    public NewsAdapter( List<NewsModel> newsList) {
        this.newsList = newsList;

    }

    public NewsAdapter(List<NewsModel> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_headline, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.headline.setText(news.getTitle());
        Glide.with(context)
                .load(news.getUrlToImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        //ui
        ImageView image;
        TextView headline,content;
        LottieAnimationView load, save, share;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            headline = itemView.findViewById(R.id.headline);
            load = itemView.findViewById(R.id.view);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);
        }
    }
}
