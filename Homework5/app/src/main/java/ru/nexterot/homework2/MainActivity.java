package ru.nexterot.homework2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.DisplayMetrics;

import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Pair<String,Double>>>, SharedPreferences.OnSharedPreferenceChangeListener {
    private final String WEATHER_URL = "http://api.coinlayer.com/live";
    private final String API_KEY = "a6a6d07fbe5912e51a96fe3ffe136cc9";

    private static final int UNIQUE_LOADER_ID = 228;

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        String currency = sharedPreferences.getString("currency", "USD");
        Log.d("tag", currency);

        recyclerView = findViewById(R.id.recycler_list);
        final LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);

        findViewById(R.id.id2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        findViewById(R.id.id3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://coinlayer.com/terms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        } else if (itemId == R.id.settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void openSettings() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<ArrayList<Pair<String,Double>>> onCreateLoader(int i, final @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<Pair<String,Double>>>(this) {
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
            public ArrayList<Pair<String,Double>> loadInBackground() {

                Log.d("tag", "lel");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String currency = sharedPreferences.getString("list", "RUB");
                Log.d("tag", "currency is " + currency);
                try {
                    Uri uriMoscow = Uri.parse(WEATHER_URL).buildUpon()
                            .appendQueryParameter("target", currency)
                            .appendQueryParameter("symbols", url)
                            .appendQueryParameter("access_key", API_KEY)
                            .build();
                    URL urlMoscow = null;
                    try {
                        urlMoscow = new URL(uriMoscow.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                    Log.d("tag", "mem");
                    String responseStr = getResponseFromHttpUrl(urlMoscow);
                    Log.d("tag", responseStr);
                    ArrayList<Pair<String,Double>> weatherStr = getWeatherFromJson(responseStr);
                    return weatherStr;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Pair<String,Double>>> loader, ArrayList<Pair<String,Double>> s) {
        // similar to onPostExecute of AsyncTask
        myAdapter.setData(s);
        Log.d("tag", "ololo");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Pair<String,Double>>> loader) {

    }

    void refresh() {
        Toast.makeText(this, "Refresh clicked!", Toast.LENGTH_SHORT).show();
        download();
    }

    void download() {
        Bundle asyncTaskLoaderParams = new Bundle();
        String samples = ((EditText)findViewById(R.id.id1)).getText().toString();
        Log.d("tag", samples);
        asyncTaskLoaderParams.putString("data", samples);
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

    ArrayList<Pair<String,Double>> getWeatherFromJson(String jsonStr) {
        try {
            JSONObject weatherObj = new JSONObject(jsonStr);
            JSONObject weatherRates = weatherObj.getJSONObject("rates");
            Iterator<String> itKeys = weatherRates.keys();
            ArrayList<Pair<String,Double>> keys = new ArrayList<>();
            while (itKeys.hasNext()) {
                String nextKey = itKeys.next();
                Double t = weatherRates.getDouble(nextKey);
                keys.add(new Pair<>(nextKey, t));
            }
            return keys;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("tag", "MainActivity: onSharedPreferenceChanged");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}

