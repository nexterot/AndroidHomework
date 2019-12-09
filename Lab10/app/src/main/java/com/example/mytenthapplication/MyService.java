package com.example.mytenthapplication;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends IntentService {
    private MediaPlayer mediaPlayer;

    public MyService() {
        super("testService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("service", "start onHandleIntent");
        mediaPlayer = MediaPlayer.create(this, R.raw.la_noire);
        try {
            Log.d("service", "start music");
            mediaPlayer.start();
            Thread.sleep(29000);
        }
        catch (Exception e) {
            Log.d("service", "unexpected exception");
        }
        Log.d("service", "stop onHandleIntent");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "start service");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);

    }
}
