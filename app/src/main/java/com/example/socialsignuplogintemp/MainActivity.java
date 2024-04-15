package com.example.socialsignuplogintemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView googleBtn, appleBtn, googleClassBtn, googleOneTapBtn, googleOneTap2024Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReferences();

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), GoogleActivity.class));
            }
        });
        googleClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), GoogleClassActivity.class));
            }
        });
        googleOneTapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), GoogleOneTapActivity.class));
            }
        });

        googleOneTap2024Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), GoogleOneTap2024Activity.class));
            }
        });
    }

    private void setReferences() {
        googleBtn = findViewById(R.id.google_btn);
        appleBtn = findViewById(R.id.apple_btn);
        googleClassBtn = findViewById(R.id.google_class_btn);
        googleOneTapBtn = findViewById(R.id.google_one_tap_btn);
        googleOneTap2024Btn = findViewById(R.id.google_one_tap_2024_btn);
    }
}