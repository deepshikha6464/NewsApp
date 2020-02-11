package com.example.anew.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.anew.NetworkUtil.NetworkCall;
import com.example.anew.model.NewsModel;
import com.example.anew.roomDB.NewsDAO;
import com.example.anew.roomDB.NewsDatabase;
import com.example.anew.roomDB.NewsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewsRepository {
    private static final String TAG = "NewsRepository";
    private NewsDAO mNewsDao;
  //  private LiveData<List<NewsModel>> newsModels;
    public static ArrayList<NewsModel> newsModelList = null;

    private LiveData<List<NewsModel>> mAllData;

    public NewsRepository(Application application) {
         NewsDatabase mdb = NewsDatabase.getDatabase(application);
        this.mNewsDao = mdb.newsDAO();
       mAllData = mNewsDao.getAllData();
    }

    public LiveData<List<NewsModel>> getAllData() {
       // mAllData = mNewsDao.getAllData();//repository has access to the DAO, it can make calls to the data access methods
        return mAllData;
    }

//  non-UI thread to avoide app crash

    public void insert(NewsModel dataItem) {
        new insertAsyncTask(mNewsDao).execute(dataItem);
    }

    private static class insertAsyncTask extends AsyncTask<NewsModel, Void, Void> {
        private NewsDAO mAsyncTaskDao;
        insertAsyncTask(NewsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NewsModel... params) {
            mAsyncTaskDao.insertItem(params[0]);
            return null;
        }
    }

    public void deleteItem(NewsModel dataItem) {
        new deleteAsyncTask(mNewsDao).execute(dataItem);
    }

    private static class deleteAsyncTask extends AsyncTask<NewsModel, Void, Void> {
        private NewsDAO mAsyncTaskDao;
        deleteAsyncTask(NewsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NewsModel... params) {
            mAsyncTaskDao.deleteItem(params[0]);
            return null;
        }
    }
//get data
   public List<NewsModel> getAllSavedNews()
   {
       List<NewsModel> saveNews = null;
       try {
           saveNews = new getSavedNews(mNewsDao).execute().get();
       } catch (ExecutionException e) {
           e.printStackTrace();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       return saveNews;
   }
private static class getSavedNews extends AsyncTask<Void,Void,List<NewsModel>>
{   private NewsDAO saveDAO;
    getSavedNews(NewsDAO dao)
    {
        saveDAO = dao;
    }

    @Override
    protected List<NewsModel> doInBackground(Void... voids) {
        List<NewsModel> saveNews = saveDAO.getAllSavedNews();
        return saveNews;
    }
}


    public void NewsRepository() {
        NetworkCall.fetchNews("top-headlines", "country=us");
        Log.d(TAG, "NewsRepository: called");
        return  ;
    }

    public static ArrayList<NewsModel> dataParsing(JSONObject jsonResponse){
        String id,name,author,url,urlToImage,title,description,publishedAt,content;
              try {
            ;
            JSONObject jsonObject= new JSONObject(jsonResponse.toString());
            String status = jsonObject.getString("status");
            int totalResponse = jsonObject.getInt("totalResults");
            newsModelList = new ArrayList<>(totalResponse);
            Log.d(TAG, "dataParsing: "+ " "+ totalResponse+" "+  newsModelList.size());
            if(status.equalsIgnoreCase("ok"))
            {
                JSONArray articles = jsonObject.getJSONArray("articles");
                for (int i = 0; i < articles.length(); i++) {

                    JSONObject source = articles.getJSONObject(i).getJSONObject("source");

                    id = source.getString("id");
                    name = source.getString("name");
                    author = articles.getJSONObject(i).getString("author");
                    title = articles.getJSONObject(i).getString("title");
                    description = articles.getJSONObject(i).getString("description");
                    url = articles.getJSONObject(i).getString("url");
                    urlToImage = articles.getJSONObject(i).getString("urlToImage");
                    publishedAt = articles.getJSONObject(i).getString("publishedAt");
                    content = articles.getJSONObject(i).getString("content");
                  NewsModel newsModel = new NewsModel(id,name,author,title,description,url,urlToImage,publishedAt,content);
                  newsModelList.add(newsModel);
                }
                Log.d(TAG, "dataParsing: "+ newsModelList.size());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsModelList;
    }
   }
