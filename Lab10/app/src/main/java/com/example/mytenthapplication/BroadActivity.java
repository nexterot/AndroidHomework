package com.example.mytenthapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BroadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad);

        Intent parenIntent = getIntent();
        if (parenIntent.hasExtra("My data")) {
            Log.d("myBroadRec", "it's working");
            String parenString = parenIntent.getStringExtra("My data");
            Toast.makeText(this, parenString, Toast.LENGTH_SHORT).show();
        }
    }
}
