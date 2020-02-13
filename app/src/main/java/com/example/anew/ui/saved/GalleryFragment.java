package com.example.anew.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private NewsViewModel newsViewModel;
    private SaveAdapter saveAdapter;
    private ArrayList<NewsModel> mNotes = new ArrayList<>();

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
    }

    private void observerSetup() {

        newsViewModel.getAllSavedData().observe(this, new Observer<List<NewsModel>>() {
            @Override
            public void onChanged(@Nullable List<NewsModel> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                saveAdapter.notifyDataSetChanged();
            }
        });
       }

    private void RecyclerViewSetup()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        saveAdapter = new SaveAdapter(mNotes,getActivity());
        recyclerView.setAdapter(saveAdapter);
        saveAdapter.notifyDataSetChanged();
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            newsViewModel.deleteItem(mNotes.get(viewHolder.getAdapterPosition()));

        }
    };
}