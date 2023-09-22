package com.example.socialsignuplogintemp.models;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * @Author: naftalikomarovski
 * @Date: 2023/09/20
 */
public class GoogleService {

    private static final String TAG = "Test_code";

    private Context context;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount gsa;


    public GoogleService(Context context) {
        this.context = context;
        initGoogleAccount();
    }

    private void initGoogleAccount() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        gsc = GoogleSignIn.getClient(context, gso);
    }

    public GoogleSignInOptions getGoogleSignInOptions() {
        return gso;
    }

    public void setGoogleSignInOptions(GoogleSignInOptions gso) {
        this.gso = gso;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return gsc;
    }

    public void setGoogleSignInClient(GoogleSignInClient gsc) {
        this.gsc = gsc;
    }

    public GoogleSignInAccount getGoogleSignInAccount() {
        gsa = GoogleSignIn.getLastSignedInAccount(context);
        return gsa;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount gsa) {
        this.gsa = gsa;
    }
}
