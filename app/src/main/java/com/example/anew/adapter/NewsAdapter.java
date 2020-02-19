package com.example.anew.adapter;

import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.anew.R;
import com.example.anew.model.NewsModel;
import com.example.anew.roomDB.NewsViewModel;
import com.example.anew.sessionManager;
import com.example.anew.ui.login.ToolsFragment;
import com.example.anew.ui.news.HomeFragment;


import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter< NewsAdapter.NewsViewHolder> {
    private static final String TAG = "NewsAdapter";
    List<NewsModel> newsList ;
    Context context;
    NewsViewModel newsViewModel;
    private static ItemClickListener mClickListener;
sessionManager session;
    public NewsAdapter(Context context) {
//        this.newsList = newsList;
this.context = context;
    }

    public NewsAdapter(List<NewsModel> newsList, Context context ,sessionManager session) {
        this.newsList = newsList;
        this.context = context;
        this.session = session;

    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flip_animation, parent, false);
        newsViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(NewsViewModel.class);
        return new NewsViewHolder(view,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        final NewsModel news = newsList.get(position);
         holder.headline.setText(news.getTitle());
        Glide.with(context)
                .load(news.getUrlToImage())
                .into(holder.image);
        holder.content.setText(news.getDescription());

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn()) {
                    holder.setIsRecyclable(false);
                    holder.save.playAnimation();
                    newsViewModel.insertItem(news);
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSave " + newsViewModel.getAllData());
                }
                else
                {
                    Toast.makeText(context, "Login to save News", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.load.playAnimation();
                String url = news.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.share.playAnimation();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = news.getUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        //ui
        ImageView image;
        TextView headline,content;
        LottieAnimationView load, save, share;
        ItemClickListener itemClickListener;

        ImageView down, up;
        LinearLayout imageControl, buttonControl;

        public NewsViewHolder(@NonNull View itemView, ItemClickListener mClickListener) {
            super(itemView);
           image = itemView.findViewById(R.id.imageView);
            headline = itemView.findViewById(R.id.headline);
            load = itemView.findViewById(R.id.view);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);
            content = itemView.findViewById(R.id.content);
            imageControl = itemView.findViewById(R.id.imageControl);
            buttonControl = itemView.findViewById(R.id.buttonContol);
            up = itemView.findViewById(R.id.up_arrow);
            down = itemView.findViewById(R.id.down);
            itemClickListener = mClickListener;

            itemView.setOnClickListener(this);

                   }

        @Override
        public void onClick(View v) {

            //myEasyFlipView.flipTheView();
          if(down.getVisibility() == View.VISIBLE)
          {
              headline.setVisibility(View.GONE);
              content.setVisibility(View.VISIBLE);
              down.setVisibility(View.GONE);
              up.setVisibility(View.VISIBLE);

          }
          else

          {       headline.setVisibility(View.VISIBLE);
                  content.setVisibility(View.GONE);
                  up.setVisibility(View.GONE);
                  down.setVisibility(View.VISIBLE);
          }
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
