package ru.nexterot.homework2;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private boolean lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            Toast.makeText(this, "Refresh clicked!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.switch_language) {
            switchLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void switchLanguage() {
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        if (lang) {
            config.setLocale(new Locale("ru"));
            Log.d("ggg", "true");
        } else {
            config.setLocale(new Locale("en"));
            Log.d("ggg", "false");
        }
        lang = !lang;
        res.updateConfiguration(config, null);
    }
}
