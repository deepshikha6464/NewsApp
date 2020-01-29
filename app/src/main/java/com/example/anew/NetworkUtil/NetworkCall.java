package com.example.anew.NetworkUtil;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.anew.NetworkUtil.constants.API;
import static com.example.anew.NetworkUtil.constants.API_KEY;
import static com.example.anew.Repository.NewsRepository.dataParsing;

/**
 * this class calls the api and
 * gets the data in form of json
 * stores in NewsModel
 */
public class NetworkCall {
    private static final String TAG = "NetworkCall";

    public static void fetchNews(String endpoint, String query){

       String url = API+endpoint+"?"+query+"&apiKey="+API_KEY;
      //  String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=ab5c10fe89da4cb799e0647ab64ac1f4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                             dataParsing(response);
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


}
