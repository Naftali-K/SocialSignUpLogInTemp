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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * https://youtu.be/suVgcrPwYKQ?si=RHmz0iTyClT96dB9 - lesson video
 * https://developers.google.com/identity/sign-in/android/start-integrating?hl=ru
 *
 * https://console.cloud.google.com/welcome?project=socialsignuplogintemp
 * - socialsignuplogintemp - project
 * 1) Dashboard
 * 2) APIs & Services -> OAuth consent screen  - set information
 * 3) APIs & Services -> Credentials -> CREATE CREDENTIALS -> OAth client ID -> Android - set information
 *      - SHA-1 certificate: Gradle -> Tasks -> android -> signingReport
 *      Alias: AndroidDebugKey
 * MD5: 3B:0E:92:xx:xx:xx:xx:xx:xx:xx:xx:DE:9F:09:92:89
 * SHA1: 7F:80:77:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:0B:8B:34:57:CC:0A:A5
 * SHA-256: 93:17:D5:B8:0A:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:78:32:68:78:46:69:3D:4C:15:6E:5F:77:06:EE:C1
 */

public class GoogleActivity extends AppCompatActivity {

    private static final String TAG = "Test_code";

    private ImageView userPhotoIv;
    private TextView userIdTv, displayNameTv, givenNameTv, familyNameTv, emailTv, idTokenTv;
    private Button signUpBtn, logOutBtn;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount gsa;

    private ActivityResultLauncher googleResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setReferences();
        setGoogleAccount();
        setResultLauncher();

        if (gsa != null) {
            showValues();
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = gsc.getSignInIntent();
                googleResultLauncher.launch(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void setGoogleAccount() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        gsa = GoogleSignIn.getLastSignedInAccount(this);
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
        gsa = GoogleSignIn.getLastSignedInAccount(this);

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