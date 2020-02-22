package com.example.anew;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.anew.Repository.NewsRepository;
import com.example.anew.ui.news.HomeFragment;

import android.preference.PreferenceManager;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.anew.sessionManager.IMAGE_URL;
import static com.example.anew.sessionManager.KEY_EMAIL;
import static com.example.anew.sessionManager.KEY_NAME;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    NewsRepository newsRepository;
    SearchView searchView;
    ImageView imageView;
    TextView name, email;
    sessionManager sessionManager;
    GoogleApiClient mGoogleApiClient;
    DrawerLayout mDrawerLayout;
   public static MenuItem logout;
   public static boolean netStatus = false;

      @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);

           sessionManager = new sessionManager(getApplicationContext());

           Toolbar toolbar = findViewById(R.id.toolbar);
           setSupportActionBar(toolbar);
           mDrawerLayout = findViewById(R.id.drawer_layout);
//search bar

           DrawerLayout drawer = findViewById(R.id.drawer_layout);
           NavigationView navigationView = findViewById(R.id.nav_view);
           Menu menu = navigationView.getMenu();


           View headerView = navigationView.getHeaderView(0);
       // TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);

        imageView =headerView.findViewById(R.id.imageViewHeader);
        name = headerView.findViewById(R.id.nameHeader);
        email =headerView.findViewById(R.id.textView);

        // Passing each menu ID as a set of Ids because each
           // menu should be considered as top level destinations.
           mAppBarConfiguration = new AppBarConfiguration.Builder(
                   R.id.nav_news, R.id.nav_gallery,
                   R.id.nav_login, R.id.nav_settings)
                   .setDrawerLayout(drawer)
                   .build();
           NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
           NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
           NavigationUI.setupWithNavController(navigationView, navController);


        logout = menu.findItem(R.id.nav_logout);
        if(sessionManager.isLoggedIn()==true)
        {
            logout.setEnabled(true);
            setUserDetail();
        }
        else
        {
            logout.setEnabled(false);
        }

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                searchView.clearFocus();
                Intent intent = new Intent();
                intent.setAction("query");
                intent.putExtra("query", query);
                sendBroadcast(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                return false;
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
               return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

       if ( !searchView.isIconified())
        {

            searchView.onActionViewCollapsed();
            this.getFragmentManager().popBackStack();
            HomeFragment.buttonContainer.setVisibility(View.VISIBLE);

        }
        else
            {

            super.onBackPressed();
        }


    }
    public void setUserDetail()
    {
        String n = sessionManager.pref.getString(KEY_NAME, "");
        String e = sessionManager.pref.getString(KEY_EMAIL, "");
        String img = sessionManager.pref.getString(IMAGE_URL, "");
            name.setText(n);
            email.setText(e);
            Glide.with(this)
                    .load(img)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);    }

    public void logout(MenuItem item) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                       sessionManager.logoutUser();
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                              mDrawerLayout.closeDrawer(GravityCompat.START);
                            onBackPressed();
                            Toast.makeText(getApplicationContext(),"Logged Out " , Toast.LENGTH_SHORT).show();
                            logout.setEnabled(false);
                        }

                    }
                });
    }

    public static boolean isOnline(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}

