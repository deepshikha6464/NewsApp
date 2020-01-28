package com.example.anew.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anew.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.example.anew.api.constants.API;
import static com.example.anew.api.constants.API_KEY;

public class FetchNews {
    private static final String TAG = "FetchNews";

    public static void headLine(String endpoint, String query){
       String url = API+endpoint+"?"+query+"&apiKey="+API_KEY;
      //  String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=ab5c10fe89da4cb799e0647ab64ac1f4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            //jsonObject = response.getJSONObject("ok");
                            JSONObject jsonObject= new JSONObject(response.toString());
                            String status = jsonObject.getString("status");
                            int totalResponse = jsonObject.getInt("totalResults");
                            JSONArray articles = jsonObject.getJSONArray("articles");
                            for (int i=0; i < articles.length(); i++) {

                                JSONObject source = articles.getJSONObject(i).getJSONObject("source");

                                String id = source.getString("id");
                                String name = source.getString("name");
                             //todo   String author = articles.getString("author").toString();
                                Log.d(TAG, "onResponse: " + id+name);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error);
                    }
                }
        );

        NetworkCall.getInstance().addToRequestQueue(jsonObjectRequest, "headLine");
    }
}
