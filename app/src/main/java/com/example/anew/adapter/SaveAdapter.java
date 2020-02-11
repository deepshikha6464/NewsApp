package com.example.anew.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.anew.R;
import com.example.anew.model.NewsModel;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.SaveViewHolder>  {
    private List<NewsModel> savedNews;

    public SaveAdapter(List<NewsModel> savedNews) {
        this.savedNews = savedNews;
    }

    @NonNull
    @Override
    public SaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flip_animation, parent, false);
        SaveViewHolder myViewHolder = new SaveViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaveViewHolder holder, int position) {
        final NewsModel news = savedNews.get(position);
        holder.headline.setText(news.getTitle());
//        Glide.with(context)
//                .load(news.getUrlToImage())
//                .into(holder.image);
//        holder.content.setText(news.getDescription());
//
//        holder.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.save.playAnimation();
//                newsViewModel.insertItem(news);
////               newsViewModel.getAllData().observe((LifecycleOwner) context, new Observer<List<NewsModel>>() {
////                   @Override
////                   public void onChanged(List<NewsModel> newsModels) {
////
////                   }
////               });
//                Toast.makeText(context,"saved",Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onSave " + newsViewModel.getAllData());
//            }
//        });
//        holder.load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.load.playAnimation();
//                String url = news.getUrl();
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                context.startActivity(i);
//            }
//        });
//
//
//        holder.share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.share.playAnimation();
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = news.getUrl();
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
//            }
//        });
//

    }

    public void setSavedNews(List<NewsModel> news) {
        savedNews = news;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return savedNews.size();
    }

    public  class SaveViewHolder extends RecyclerView.ViewHolder{
        //ui
        ImageView image;
        TextView headline,content;
        LottieAnimationView load, save, share;
        NewsAdapter.ItemClickListener itemClickListener;
        EasyFlipView myEasyFlipView;
        LinearLayout imageControl, buttonControl;
        public SaveViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            headline = itemView.findViewById(R.id.headline);
            load = itemView.findViewById(R.id.view);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);
            content = itemView.findViewById(R.id.content);
            imageControl = itemView.findViewById(R.id.imageControl);
            buttonControl = itemView.findViewById(R.id.buttonContol);
           // itemClickListener = mClickListener;
            myEasyFlipView = itemView.findViewById(R.id.myEasyFlipView);
           // itemView.setOnClickListener(this);
        }
    }
}
