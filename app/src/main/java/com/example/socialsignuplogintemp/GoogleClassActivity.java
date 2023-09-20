package com.example.socialsignuplogintemp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialsignuplogintemp.models.GoogleService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GoogleClassActivity extends AppCompatActivity {

    private static final String TAG = "Test_code";

    private ImageView userPhotoIv;
    private TextView userIdTv, displayNameTv, givenNameTv, familyNameTv, emailTv, idTokenTv;
    private Button signUpBtn, logOutBtn;

    private GoogleService googleService;
    private GoogleSignInAccount gsa;
    private ActivityResultLauncher googleResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setReferences();
        setResultLauncher();

        googleService = new GoogleService(this);
        gsa = googleService.getGoogleSignInAccount();

        if (gsa != null) {
            Log.d(TAG, "onCreate: User already login. Show info");
            showValues();
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleService.getGoogleSignInClient().getSignInIntent();
                googleResultLauncher.launch(intent);
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleService.getGoogleSignInClient().signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                        finish();
                    }
                });
            }
        });
    }

    private void setReferences() {
        userPhotoIv = findViewById(R.id.user_photo_iv);
        userIdTv = findViewById(R.id.user_id_tv);
        displayNameTv = findViewById(R.id.display_name_tv);
        givenNameTv = findViewById(R.id.given_name_tv);
        familyNameTv = findViewById(R.id.family_name_tv);
        emailTv = findViewById(R.id.email_tv);
        idTokenTv = findViewById(R.id.id_token_tv);
        signUpBtn = findViewById(R.id.sign_up_btn);
        logOutBtn = findViewById(R.id.log_out_btn);
    }

    private void setResultLauncher() {
        googleResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        task.getResult(ApiException.class);
                        Log.d(TAG, "onActivityResult: Have result");
                        showValues();
                    } catch (ApiException e) {
                        Log.d(TAG, "onActivityResult: Result Error: " + e.getLocalizedMessage());
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void showValues() {
        gsa = googleService.getGoogleSignInAccount();

        Log.d(TAG, "showValues: Start show inf");

        if (gsa != null) {
            String personIdToken = gsa.getIdToken();
            String personName = gsa.getDisplayName();
            String personGivenName = gsa.getGivenName();
            String personFamilyName = gsa.getFamilyName();
            String personEmail = gsa.getEmail();
            String personID = gsa.getId();
            Uri personPhoto = gsa.getPhotoUrl();

            String authCode = gsa.getServerAuthCode();

            Log.d(TAG, "updateUI: \nID Token: " + personIdToken
                    + "\nPerson Name: " + personName
                    + "\nPerson Given Name: " + personGivenName
                    + "\nPerson Family Name: " + personFamilyName
                    + "\nPerson Email: " + personEmail
                    + "\nPerson ID: " + personID
//                    + "\nPerson Photo: " + personPhoto.toString()
                    + "\nAuth Code: " + authCode);

            userIdTv.setText(personID);
            displayNameTv.setText(personName);
            givenNameTv.setText(personGivenName);
            familyNameTv.setText(personFamilyName);
            emailTv.setText(personEmail);
            idTokenTv.setText(personIdToken);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}