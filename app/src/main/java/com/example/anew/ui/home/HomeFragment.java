package com.example.anew.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import static com.example.anew.NetworkUtil.constants.API;
import static com.example.anew.NetworkUtil.constants.API_KEY;
import static com.example.anew.Repository.NewsRepository.dataParsing;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
RecyclerView recyclerView;
NewsAdapter mNewsAdapter;
List<NewsModel> newsModels;


//    private HomeViewModel homeViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
                           //  ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_news, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);


//         homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
       // return root;
   // }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,
                container, false);
              recyclerView = view.findViewById(R.id.rv_news);
               RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        fetchNews("top-headlines", "country=us");
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

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error);
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

}