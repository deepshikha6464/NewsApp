package com.example.anew.NetworkUtil;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkApplication extends Application {

    private static final String TAG = "NetworkApplication";

    //Declare a private RequestQueue variable
    private RequestQueue requestQueue;
    private static NetworkApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized NetworkApplication getInstance()
    {
        return mInstance;
    }

     /*
    Create a getRequestQueue() method to return the instance of
    RequestQueue.This kind of implementation ensures that
    the variable is instatiated only once and the same
    instance is used throughout the application
    */
     public RequestQueue getRequestQueue()
     {
         if(requestQueue==null)
         requestQueue = Volley.newRequestQueue(getApplicationContext());
         return requestQueue;
     }

      /*
    public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */

        public void addToRequestQueue (Request request , String tag)
        {
            request.setTag(tag);
            getRequestQueue().add(request);
        }

        /*
    Cancel all the requests matching with the given tag
    */
        public void cancelAllRequest(String tag)
        {
            getRequestQueue().cancelAll(tag);
        }
}

