package com.example.mytenthapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MyBroadcastReceiver broadcastReceiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.player);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            }
        });

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        broadcastReceiver = new MyBroadcastReceiver();
        this.registerReceiver(broadcastReceiver, filter);

        Button button2 = (Button) findViewById(R.id.broadcast);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadIntent = new Intent(MainActivity.this, BroadActivity.class);
                broadIntent.setAction("com.example.broadcast.SPEC_NOTIFICATION");
                broadIntent.putExtra("My data", "Some data here!");
                sendBroadcast(broadIntent);
                startActivity(broadIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(broadcastReceiver, filter);
        super.onResume();
    }
}
