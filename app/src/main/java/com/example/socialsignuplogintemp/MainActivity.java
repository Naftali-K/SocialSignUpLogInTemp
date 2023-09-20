package com.example.socialsignuplogintemp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView googleBtn, appleBtn, googleClassBtn;

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
    }

    private void setReferences() {
        googleBtn = findViewById(R.id.google_btn);
        appleBtn = findViewById(R.id.apple_btn);
        googleClassBtn = findViewById(R.id.google_class_btn);
    }
}