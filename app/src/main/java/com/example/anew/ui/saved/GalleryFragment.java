package com.example.anew.ui.saved;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RenderNode;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.anew.sessionManager;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    RecyclerView recyclerView;
    private NewsViewModel newsViewModel;
    private SaveAdapter saveAdapter;
    public static LinearLayout nosaveLayout;
    private ArrayList<NewsModel> mNotes = new ArrayList<>();
    sessionManager  session ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        session = new sessionManager(getActivity());
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.rv_save);
        nosaveLayout = root.findViewById(R.id.nosaveLayout);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        observerSetup();
        RecyclerViewSetup();
        if(session.isLoggedIn() ) {

                recyclerView.setVisibility(View.VISIBLE);
                nosaveLayout.setVisibility(View.GONE);
          }
        else
        {
           recyclerView.setVisibility(View.GONE);
           nosaveLayout.setVisibility(View.VISIBLE);
        }

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
        saveAdapter = new SaveAdapter(mNotes,getActivity(),session);
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

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.my_background))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}