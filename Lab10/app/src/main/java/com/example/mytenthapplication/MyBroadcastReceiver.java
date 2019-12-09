package com.example.mytenthapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public MyBroadcastReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadRec", "it's working");
        Toast.makeText(context, "Action:" + intent.getAction(), Toast.LENGTH_SHORT).show();
    }
}
