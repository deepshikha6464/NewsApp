package com.example.anew.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.example.anew.MainActivity;
import com.example.anew.R;
import com.example.anew.sessionManager;
import com.example.anew.ui.news.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class ToolsFragment extends Fragment {
    private static final String TAG = "ToolsFragment";
    private ToolsViewModel toolsViewModel;
   public static  GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN;

    //ui
    TextView tv;
    SignInButton signInButton;
    LottieAnimationView sucess;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        tv= root.findViewById(R.id.tv);
         sucess= root.findViewById(R.id.success);


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

// Set the dimensions of the sign-in button.
        signInButton = root.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        //click llictener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
               return root;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account != null)
            {
                sessionManager sm  = new sessionManager(getActivity());
                sm.createLoginSession(account.getDisplayName(),account.getEmail(),account.getPhotoUrl().toString(),true);
                sucess.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),"Login sucess" ,Toast.LENGTH_SHORT).show();
                signInButton.setVisibility(View.GONE);
                MainActivity.logout.setEnabled(true);
                getActivity().onBackPressed();

            }
            Log.d(TAG, "handleSignInResult: " +account.getEmail() + account.getDisplayName() + account.getPhotoUrl()+ " "+ account.isExpired());

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chkSignIn();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        chkSignIn();

    }


    public  void chkSignIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account !=null)
        { signInButton.setVisibility(View.GONE);
            sucess.setVisibility(View.GONE);
            sessionManager sm  = new sessionManager(getActivity());
            sm.createLoginSession(account.getDisplayName(),account.getEmail(),account.getPhotoUrl().toString(),true);
            tv.setText("Hey"+" "+account.getDisplayName() +" you are already LoggedIn! ");
            tv.setVisibility(View.VISIBLE);


        }
    }

}