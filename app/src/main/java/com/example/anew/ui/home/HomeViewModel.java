package com.example.anew.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anew.NetworkUtil.NetworkCall;
import com.example.anew.Repository.NewsRepository;
import com.example.anew.model.NewsModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<NewsModel>> newsLiveData;
    ArrayList<NewsModel> newsList;


    public HomeViewModel() {

        newsLiveData = new MutableLiveData<>();

//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");

    }

    public MutableLiveData<ArrayList<NewsModel>> getNewsLiveData() {
        return newsLiveData;
    }

    public void init(){
       // NewsRepository newsRepository = new NewsRepository();
        //ArrayList<>

        newsLiveData.setValue(newsList);
    }

   // public LiveData<String> getText() {
     //   return mText;
   // }
}