package com.example.anew.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anew.R;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.rv_save);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        listItem();
        return root;
    }

public void listItem()
{
    list.add(1);
    list.add(1);
    list.add(1);
    list.add(1);
    list.add(1);
    list.add(1);
    list.add(1);
    list.add(1);
    list.add(1);
}

}