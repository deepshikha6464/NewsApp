package com.example.anew.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.anew.Repository.NewsRepository;
import com.example.anew.model.NewsModel;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepository mNewsRepository;
    private LiveData<List<NewsModel>> mListLiveData;

        public NewsViewModel(@NonNull Application application) {
        super(application);
        mNewsRepository = new NewsRepository((application));
        mListLiveData = mNewsRepository.getAllData();
    }

    public LiveData<List<NewsModel>> getAllData() {
        return mListLiveData;
    }
    public LiveData<List<NewsModel>> getAllSavedData()
    {
        return mNewsRepository.getAllSavedNews();
    }

    public void insertItem(NewsModel dataItem) {
        mNewsRepository.insert(dataItem);
    }

    public void deleteItem(NewsModel dataItem) {
        mNewsRepository.deleteItem(dataItem);
    }

}
