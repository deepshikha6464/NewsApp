package com.example.anew.Repository;

import android.util.Log;

import com.example.anew.NetworkUtil.NetworkCall;
import com.example.anew.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository {
    private static final String TAG = "NewsRepository";
    public static List<NewsModel> newsModelList;

    public NewsRepository() {
        NetworkCall.fetchNews("top-headlines", "country=us");
        Log.d(TAG, "NewsRepository: called");
    }

    public static void dataParsing(JSONObject jsonResponse){
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
                   // Log.d(TAG, "onResponse: " + " " + title);
                    NewsModel newsModel = new NewsModel(id,name,author,title,description,url,urlToImage,publishedAt,content);
                  newsModelList.add(newsModel);
                }
                Log.d(TAG, "dataParsing: "+ newsModelList.size());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
   }
