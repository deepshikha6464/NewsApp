package com.example.anew.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anew.R;
import com.example.anew.adapter.SaveAdapter;
import com.example.anew.model.NewsModel;
import com.example.anew.roomDB.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    RecyclerView recyclerView;
    List<NewsModel> mNewsModels;
    List<Integer> list = new ArrayList<>() ;
    private NewsViewModel newsViewModel;
    private SaveAdapter saveAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.rv_save);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        observerSetup();
        RecyclerViewSetup();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
//        observerSetup();
//        RecyclerViewSetup();
    }

    private void observerSetup() {
//for getting data and sending to adapter
//        newsViewModel.getAllData().observe(this, new Observer<List<NewsModel>>() {
//            @Override
//            public void onChanged(@Nullable final List<NewsModel> news) {
//                saveAdapter.setSavedNews(news);
//            }
//        });
        mNewsModels = newsViewModel.getAllSavedData();
        saveAdapter = new SaveAdapter(mNewsModels);

    }

    private void RecyclerViewSetup()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(saveAdapter);

    }
}