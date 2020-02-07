package com.example.anew.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anew.MainActivity;
import com.example.anew.NetworkUtil.NetworkApplication;
import com.example.anew.NetworkUtil.NetworkCall;
import com.example.anew.R;
import com.example.anew.Repository.NewsRepository;
import com.example.anew.adapter.NewsAdapter;
import com.example.anew.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;
import static com.example.anew.NetworkUtil.constants.API;
import static com.example.anew.NetworkUtil.constants.API_KEY;
import static com.example.anew.Repository.NewsRepository.dataParsing;


public class HomeFragment extends Fragment implements NewsAdapter.ItemClickListener, OnClickListener {
    private static final String TAG = "HomeFragment";
RecyclerView recyclerView;
NewsAdapter mNewsAdapter;
List<NewsModel> newsModels;
Button head,gen,sci,spo,tech,busi,ent,hel;
String query;
public static  LinearLayout buttonContainer;
LottieAnimationView rvPlaceHolder,rvNoData,newData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,
                container, false);
        //ui for buttons
        head = view.findViewById(R.id.headline);
        ent = view.findViewById(R.id.entertainment);
        spo = view.findViewById(R.id.sports);
        sci = view.findViewById(R.id.science);
        busi = view.findViewById(R.id.business);
        tech = view.findViewById(R.id.technology);
        hel = view.findViewById(R.id.health);
        buttonContainer = view.findViewById(R.id.button_container);

        rvPlaceHolder = view.findViewById(R.id.rvPlaceHolder);
        rvNoData = view.findViewById(R.id.rvNoData);
        newData = view.findViewById(R.id.rvNewData);

        head.setOnClickListener(this);
       ent.setOnClickListener(this);
       spo.setOnClickListener(this);
       sci.setOnClickListener(this);
       busi.setOnClickListener(this);
       tech.setOnClickListener(this);
       hel.setOnClickListener(this);

       //search view query
        IntentFilter filter = new IntentFilter("query");
        getActivity().registerReceiver(new Receiver(), filter);
        recyclerView = view.findViewById(R.id.rv_news);
               RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

         recyclerView.setVisibility(GONE);
         rvPlaceHolder.setVisibility(VISIBLE);
         newData.setVisibility(GONE);
        //fetchNews("everything", "");
        fetchNews("top-headlines", "country=in");


                       return view;


    }


    public  void fetchNews(String endpoint, String query){


        String url = API+endpoint+"?"+query+"&apiKey="+API_KEY;
        //  String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=ab5c10fe89da4cb799e0647ab64ac1f4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dataParsing(response);
                        mNewsAdapter = new NewsAdapter(newsModels, getActivity());
                        recyclerView.setAdapter(mNewsAdapter);
                        recyclerView.setVisibility(VISIBLE);
                        rvPlaceHolder.setVisibility(GONE);
                        newData.setVisibility(GONE);
                        rvNoData.setVisibility(GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error);
                        recyclerView.setVisibility(GONE);
                        rvNoData.setVisibility(VISIBLE);
                        rvPlaceHolder.setVisibility(GONE);
                    }
                }
        );

        NetworkApplication.getInstance().addToRequestQueue(jsonObjectRequest, "fetchNews");
          }

    public  void dataParsing(JSONObject jsonResponse){
        String id,name,author,url,urlToImage,title,description,publishedAt,content;
        try {
            ;
            JSONObject jsonObject= new JSONObject(jsonResponse.toString());
            String status = jsonObject.getString("status");
            int totalResponse = jsonObject.getInt("totalResults");
            newsModels = new ArrayList<>(totalResponse);
            Log.d(TAG, "dataParsing: "+ " "+ totalResponse+" "+  newsModels.size());
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
                    newsModels.add(newsModel);
                }
                Log.d(TAG, "dataParsing: "+ newsModels.size());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: ");
    }
//gives category - part of query in api
    private String getCategory(int itemResId) {
        String category = null ;
        switch (itemResId) {
            case R.id.headline:
                category = "general";
                break;
//            case R.id.general:
//                category = "general";
//                break;
            case R.id.entertainment:
                category = "entertainment";
                break;
            case R.id.technology:
                category = "technology";
                break;
            case R.id.sports:
                category = "sports";
                break;
            case R.id.science:
                category = "science";
                break;
            case R.id.business:
                category = "business";
                break;
                case R.id.health:
                category = "health";
                break;
        }
        return category;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
       query=  makeQuery(id);
        newData.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
        fetchNews("top-headlines", query);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String makeQuery(int id)
    {
        String category = getCategory(id);
        String query = "country=in&category="+category;
        return query;
    }


    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String i = intent.getAction();
          if(intent.getAction().toString() =="query")
            {
                //on search pressed
                buttonContainer.setVisibility(GONE);
                String query = intent.getExtras().getString("query");
                query = "q=" + query;
                fetchNews("everything", query);
            }

        }
    }
}