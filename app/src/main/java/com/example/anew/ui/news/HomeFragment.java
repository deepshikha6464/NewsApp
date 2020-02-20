package com.example.anew.ui.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anew.NetworkUtil.NetworkApplication;
import com.example.anew.R;
import com.example.anew.adapter.NewsAdapter;
import com.example.anew.model.NewsModel;
import com.example.anew.roomDB.NewsViewModel;
import com.example.anew.sessionManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.view.View.*;
import static com.example.anew.MainActivity.isOnline;
import static com.example.anew.NetworkUtil.constants.API;
import static com.example.anew.NetworkUtil.constants.API_KEY;


public class HomeFragment extends Fragment implements NewsAdapter.ItemClickListener, OnClickListener {

    private static final String TAG = "HomeFragment";

    //ui
    RecyclerView recyclerView;
    Chip head,gen,sci,spo,tech,busi,ent,hel;
    public static  LinearLayout buttonContainer;
    LottieAnimationView rvPlaceHolder,rvNoData,newData;
    ChipGroup chipGroup;
    View view;

    //vars
    NewsAdapter mNewsAdapter;
    List<NewsModel> newsModels;
    String query;
    boolean oncreate = false;
    sessionManager session;
    NetworkChangeReceiver networkChangeReceiver;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);
        session = new sessionManager(getActivity()); //shared preferences
        oncreate = true;
        UIdeclare(view);
        clickListeners();
        searchViewQuery();
        initRecyclerView();

        networkCall("top-headlines","country=in");  //"top-headlines", "country=in"
           return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerWifiReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
       }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: ");//adapter item click
    }

        @Override
    public void onClick(View v) {
        int id = v.getId();
       query=  makeQuery(id);
        newData.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
        if(isOnline(getActivity())) {
            fetchNews("top-headlines", query);
        }
        else
        {
            Toast.makeText(getActivity(),"NoInternet", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(GONE);
            rvNoData.setVisibility(VISIBLE);
            rvPlaceHolder.setVisibility(GONE);
            newData.setVisibility(GONE);
        }


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
    private void UIdeclare(View view) {
        //ui for buttons
        head = view.findViewById(R.id.headline);
        ent = view.findViewById(R.id.entertainment);
        spo = view.findViewById(R.id.sports);
        sci = view.findViewById(R.id.science);
        busi = view.findViewById(R.id.business);
        tech = view.findViewById(R.id.technology);
        hel = view.findViewById(R.id.health);
        buttonContainer = view.findViewById(R.id.button_container);
        chipGroup = view.findViewById(R.id.chip_group);

        //placeholders
        rvPlaceHolder = view.findViewById(R.id.rvPlaceHolder);
        rvNoData = view.findViewById(R.id.rvNoData);
        newData = view.findViewById(R.id.rvNewData);

        //recyclerview
        recyclerView = view.findViewById(R.id.rv_news);
    }
    private void clickListeners(){
        head.setOnClickListener(this);
        ent.setOnClickListener(this);
        spo.setOnClickListener(this);
        sci.setOnClickListener(this);
        busi.setOnClickListener(this);
        tech.setOnClickListener(this);
        hel.setOnClickListener(this);
    }
    private void searchViewQuery(){
        //search view query
        IntentFilter filter = new IntentFilter("query");
        getActivity().registerReceiver(new Receiver(), filter);
    }
    private void initRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setVisibility(GONE);
        rvPlaceHolder.setVisibility(VISIBLE);
        newData.setVisibility(GONE);
    }
    private void networkCall(String endpoint , String query) {
        String ep = endpoint;
        String q = query;
        if(isOnline(getActivity())) {
            fetchNews(ep,q);
        }
        else
        {
            Toast.makeText(getActivity(),"NoInternet", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(GONE);
            rvNoData.setVisibility(VISIBLE);
            rvPlaceHolder.setVisibility(GONE);
            newData.setVisibility(GONE);


        }
    }
    private String makeQuery(int id) {
        String category = getCategory(id);
        String query = "country=in&category="+category;
        return query;
    }
    private String getSelectedChip(){
        int i=0;
        String query = null;
        int totalChips = chipGroup.getChildCount();
        while (i < totalChips) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked() ) {
                query = "country=in&category="+chip.getText().toString();
                if(i==0)
                {
                    query = "country=in&category="+"general";
                }
            }
            i++;
        }
        return query;
    }
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
    public  void fetchNews(String endpoint, String query){


        String url = API+endpoint+"?"+query+"&apiKey="+API_KEY;
        //  String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=ab5c10fe89da4cb799e0647ab64ac1f4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dataParsing(response);
                        mNewsAdapter = new NewsAdapter(newsModels, getContext(),session);
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
    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        networkChangeReceiver = new NetworkChangeReceiver();

//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkChangeReceiver, filter);
    }
   // network
    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                if (isOnline(context) )
                {

                    query = getSelectedChip();
                    String endpoint = "top-headlines";
                    if(query == null)
                    {
                        networkCall(endpoint,"country=in");

                    }
                    else
                    {
                        networkCall(endpoint,query);
                    }
                    if(oncreate == false)
                   showSnackbar("Back Online","#3a9fbf" );

                } else
                    {
                    showSnackbar("Internet Connection Lost" ,"#ff0000" );
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
         oncreate = false;
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
private void showSnackbar(String msg,String c)
{
    Snackbar snackbar = Snackbar
            .make(getActivity().getCurrentFocus(), msg, Snackbar.LENGTH_SHORT);
    View snackBarView = snackbar.getView();
    snackBarView.setBackgroundColor(Color.parseColor(c));
    TextView tv = snackBarView.findViewById(R.id.snackbar_text);
    tv.setTextColor(WHITE);
    snackbar.show();
}
}