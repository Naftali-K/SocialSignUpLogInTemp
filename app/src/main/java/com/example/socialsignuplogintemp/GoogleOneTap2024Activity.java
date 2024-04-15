package com.example.socialsignuplogintemp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * https://youtu.be/suVgcrPwYKQ?si=zywZILQycXNeO-vl - lesson video
 * https://developers.google.com/identity/sign-in/android/legacy-sign-in - Google documentation
 */

public class GoogleOneTap2024Activity extends AppCompatActivity {

    private static final String TAG = "Test_code";

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    private TextView emailTv, userIdTv;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_one_tap2024);
        setReferences();
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void setReferences() {
        emailTv = findViewById(R.id.email_tv);
        userIdTv = findViewById(R.id.user_id_tv);
        signInButton = findViewById(R.id.sign_in_button);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            return;
        }
        String email = account.getEmail();
        emailTv.setText(email);

        String userID = account.getId();
        userIdTv.setText(userID);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResulet(task);
        }
    }

    private void handleSignInResulet(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account1 = task.getResult(ApiException.class);
            updateUI(account1);
        } catch (ApiException e) {
            Log.d(TAG, "handleSignInResulet: Failed code: " + e.getStatusCode());
            throw new RuntimeException(e);
        }
    }
}