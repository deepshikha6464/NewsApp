package com.example.anew.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anew.api.FetchNews;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        FetchNews.headLine("top-headlines", "country=us");
    }

    public LiveData<String> getText() {
        return mText;
    }
}