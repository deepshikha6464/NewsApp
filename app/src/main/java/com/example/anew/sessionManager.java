package com.example.anew;

import android.content.Context;
import android.content.SharedPreferences;

public class sessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    //shared pref mode
    int PRIVATE_MODE = 0;

    //sharedpref file name
    private static final String PREF_NAME = "newsLoginPref";

    private static final String IS_LOGIN = "isLoggedIn";

    //key name
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Email address (make variable public to access from outside)
    public static final String IMAGE_URL = "image_url";

    public static final boolean sessionExpired = false;

    public sessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(KEY_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name,String email, String url, Boolean login)
    {
        editor.putBoolean(IS_LOGIN,login);
        editor.putString(KEY_NAME ,name);
        editor.putString(KEY_EMAIL ,email);
        editor.putString(IMAGE_URL ,url);

        //commit changes

        editor.commit();
    }
}
