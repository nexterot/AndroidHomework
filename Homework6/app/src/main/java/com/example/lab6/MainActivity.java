package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    static boolean flag = true;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.spec);
        textView.setText("Text");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Boolean showBass = sharedPreferences.getBoolean("bool", true);
        String language = sharedPreferences.getString("list", "ru");

        imageView = (ImageView) findViewById(R.id.imageView);

        if (showBass)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.GONE);

        if (flag) {
            if (language.equals("ru"))
                switchLanguage("английский");
            else
                switchLanguage("en");
            flag = false;
        }

        String specText = sharedPreferences.getString("text", "");
        Boolean showText = sharedPreferences.getBoolean("switch", true);

        textView.setText(specText);
        if (showText) {
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartActivity() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        finish();
    }

    void switchLanguage (String s) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        Locale l = config.locale;
        if (s.equals("английский")) {
            config.setLocale(new Locale("ru"));
            Log.d("tag", "change lang ru");
        } else {
            config.setLocale(new Locale("en"));
            Log.d("tag", "change lang en");
        }
        res.updateConfiguration(config, dm);
        recreate();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String language = sharedPreferences.getString("list", "ru");
        Boolean showBass = sharedPreferences.getBoolean("bool", true);

        if (showBass)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.GONE);

        if (language.equals("ru"))
            switchLanguage("английский");
        else
            switchLanguage("en");

        String specText = sharedPreferences.getString("text", "123");
        Boolean showText = sharedPreferences.getBoolean("switch", true);

        textView.setText(specText);
        if (showText) {
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.GONE);
        }
    }
}
