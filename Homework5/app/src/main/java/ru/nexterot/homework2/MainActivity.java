package ru.nexterot.homework2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = "3862f0bc7ec07174878a543c189bd2a0";
    private final String MOSCOW_ID = "524901";

    private final int MODE_NO_SAVE = 1;
    private final int MODE_ON_SAVE_INSTANCE = 2;
    private final int MODE_STATIC_VAR = 3;

    private static final int UNIQUE_LOADER_ID = 228;

    private TextView weatherInfoTextView;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    static String staticVar = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherInfoTextView = findViewById(R.id.weather_info);

        recyclerView = findViewById(R.id.recycler_list);
        final LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode = getPreferences(MODE_PRIVATE).getInt("mode", -1);
                int next_mode;
                switch (mode) {
                    case MODE_NO_SAVE:
                        next_mode = MODE_ON_SAVE_INSTANCE;
                        Toast.makeText(MainActivity.this, "SAVE INSTANCE", Toast.LENGTH_SHORT).show();
                        break;
                    case MODE_ON_SAVE_INSTANCE:
                        next_mode = MODE_STATIC_VAR;
                        Toast.makeText(MainActivity.this, "STATIC VAR", Toast.LENGTH_SHORT).show();
                        break;
                    case MODE_STATIC_VAR:
                        next_mode = MODE_NO_SAVE;
                        Toast.makeText(MainActivity.this, "NO SAVE", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        next_mode = MODE_NO_SAVE;
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putInt("mode", next_mode);
                editor.apply();
            }
        });

        int mode = getPreferences(MODE_PRIVATE).getInt("mode", -1);
        if (mode == MODE_STATIC_VAR)
            weatherInfoTextView.setText(staticVar);
        if (mode == MODE_ON_SAVE_INSTANCE)
            weatherInfoTextView.setText(savedInstanceState.getString("data"));
    }

    @Override
    protected void onStart() {
        Log.d("tag", "onStart");
        TextView t = findViewById(R.id.weather_info);
        if (t == null)
            return;
        t.setText("onStart");
        super.onStart();
    }
    
    @Override
    protected void onStop() {
        Log.d("tag", "onStop");
        TextView t = findViewById(R.id.weather_info);
        if (t != null)
            t.setText("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("tag", "onDestroy");
        TextView t = findViewById(R.id.weather_info);
        if (t != null)
            t.setText("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("tag", "onPause");
        TextView t = findViewById(R.id.weather_info);
        if (t != null)
            t.setText("onPause");
        int mode = getPreferences(MODE_PRIVATE).getInt("mode", -1);
        if (mode == MODE_STATIC_VAR)
            staticVar = weatherInfoTextView.getText().toString();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("tag", "onResume");
        TextView t = findViewById(R.id.weather_info);
        if (t != null)
            t.setText("onResume");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int mode = getPreferences(MODE_PRIVATE).getInt("mode", -1);
        if (mode == MODE_ON_SAVE_INSTANCE) {
            staticVar = weatherInfoTextView.getText().toString();
            outState.putString("data", "kek");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            refresh();
            return true;
        } else if (itemId == R.id.switch_language) {
            switchLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void switchLanguage() {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        Locale l = config.locale;
        if (l.getDisplayLanguage().equals("английский")) {
            config.setLocale(new Locale("ru"));
            Log.d("tag", "change lang ru");
        } else {
            config.setLocale(new Locale("en"));
            Log.d("tag", "change lang en");
        }
        res.updateConfiguration(config, dm);
        recreate();
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, final @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String url;

            @Override
            protected void onStartLoading() {
                Log.d("tag", "kek");
                // similar ot preExecute of AsyncTask
                if (args == null) {
                    return;
                }
                Log.d("tag", "heh");
                myAdapter.clear();
                url = args.getString("data");

                // show loading indicator
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {

                Log.d("tag", "lel");
                try {
                    Uri uriMoscow = Uri.parse(WEATHER_URL).buildUpon()
                            .appendQueryParameter("id", url)
                            .appendQueryParameter("appid", API_KEY)
                            .build();
                    URL urlMoscow = null;
                    try {
                        urlMoscow = new URL(uriMoscow.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return "parsing url error";
                    }
                    Log.d("tag", "mem");
                    String responseStr = getResponseFromHttpUrl(urlMoscow);
                    String weatherStr = getWeatherFromJson(responseStr);
                    Log.d("tag", weatherStr);
                    return weatherStr;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "network error";
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        // similar to onPostExecute of AsyncTask
        myAdapter.setData(s);

        Log.d("tag", "ololo");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    void refresh() {
        Toast.makeText(this, "Refresh clicked!", Toast.LENGTH_SHORT).show();
        download();
    }

    void download() {
        //new requestInternet().execute(urlMoscow, urlPetersburg);
        Bundle asyncTaskLoaderParams = new Bundle();
        asyncTaskLoaderParams.putString("data", MOSCOW_ID);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(UNIQUE_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(UNIQUE_LOADER_ID, asyncTaskLoaderParams, this);
        } else {
            loaderManager.restartLoader(UNIQUE_LOADER_ID, asyncTaskLoaderParams, this);
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return "network error";
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    String getWeatherFromJson(String jsonStr) {
        try {
            JSONObject weatherObj = new JSONObject(jsonStr);
            JSONArray weather_predictions = weatherObj.getJSONArray("weather");
            String weather = weather_predictions.getJSONObject(0).getString("main");
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
