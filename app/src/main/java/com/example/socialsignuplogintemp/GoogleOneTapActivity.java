package com.example.socialsignuplogintemp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

/**
 * https://youtu.be/Zz3412C4BSA?si=Nzw9SFYfSdL9VRUL - lesson video
 * https://developers.google.com/identity/one-tap/android/overview - documentation
 * https://developers.google.com/identity/one-tap/android/create-new-accounts
 *
 * https://console.cloud.google.com/welcome?project=socialsignuplogintemp
 *  * - socialsignuplogintemp - project
 *  * 1) Dashboard
 *  * 2) APIs & Services -> OAuth consent screen  - set information
 *  * 3) APIs & Services -> Credentials -> CREATE CREDENTIALS -> OAth client ID -> Android - set information
 *  *      - SHA-1 certificate: Gradle -> Tasks -> android -> signingReport
 *  *      Alias: AndroidDebugKey
 *  * 4) APIs & Services -> Credentials -> CREATE CREDENTIALS -> OAth client ID -> Web-application - save web client id
 *  * MD5: 3B:0E:92:xx:xx:xx:xx:xx:xx:xx:xx:DE:9F:09:92:89
 *  * SHA1: 7F:80:77:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:0B:8B:34:57:CC:0A:A5
 *  * SHA-256: 93:17:D5:B8:0A:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:78:32:68:78:46:69:3D:4C:15:6E:5F:77:06:EE:C1
 */

public class GoogleOneTapActivity extends AppCompatActivity {

    private static final String TAG = "Test_code";

    private boolean showOneTapUI = true;

    private ImageView userPhotoIv;
    private TextView userIdTv, displayNameTv, givenNameTv, familyNameTv, emailTv, idTokenTv, passwordTv;
    private Button signUpBtn, logOutBtn;

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

    private ActivityResultLauncher<IntentSenderRequest> intentSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_one_tap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setReferences();
        setResultLauncher();

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.your_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
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
        passwordTv = findViewById(R.id.password_tv);
    }

    private void setResultLauncher() {
        intentSender = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken !=  null) {
                            Log.d(TAG, "Got ID token.");
                            setValuesOutPut(credential);
                        }
                    } catch (ApiException e) {
                        Log.d(TAG, "onActivityResult: Error: " + e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    private void signUp() {
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        intentSender.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void setValuesOutPut(SignInCredential credential) {
        String personIdToken = credential.getGoogleIdToken();
        String personName = credential.getDisplayName();
        String personGivenName = credential.getGivenName();
        String personFamilyName = credential.getFamilyName();
        String personEmail = credential.getId();
        String personID = credential.getId();
        Uri personPhoto = credential.getProfilePictureUri();
        String personPassword = credential.getPassword();

        Log.d(TAG, "updateUI: \nID Token: " + personIdToken
                + "\nPerson Name: " + personName
                + "\nPerson Given Name: " + personGivenName
                + "\nPerson Family Name: " + personFamilyName
                + "\nPerson Email: " + personEmail
                + "\nPerson ID: " + personID
                + "\nPerson Photo: " + personPhoto.toString()
                + "\nPerson Password: " + personPassword);

        userIdTv.setText(personID);
        displayNameTv.setText(personName);
        givenNameTv.setText(personGivenName);
        familyNameTv.setText(personFamilyName);
        emailTv.setText(personEmail);
        idTokenTv.setText(personIdToken);
//        passwordTv.setText(personPassword);

        Picasso.get().load(personPhoto).into(userPhotoIv);
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