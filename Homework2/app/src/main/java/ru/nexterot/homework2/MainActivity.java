package ru.nexterot.homework2;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = "3862f0bc7ec07174878a543c189bd2a0";
    private final String MOSCOW_ID = "524901";
    private final String SAINT_PETERSBURG_ID = "498817";

    private TextView weatherInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherInfoTextView = findViewById(R.id.weather_info);
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

    public class requestInternet extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            try {
                String responseStr = getResponseFromHttpUrl(urls[0]);
                Log.d("tag", responseStr);
                String weatherStr = getWeatherFromJson(responseStr);
                Log.d("tag", weatherStr);
                return weatherStr;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            weatherInfoTextView.setText(String.format(getString(R.string.weather_description), s));
            super.onPostExecute(s);
        }
    }

    void refresh() {
        Toast.makeText(this, "Refresh clicked!", Toast.LENGTH_SHORT).show();
        download();
    }

    void download() {
        Uri uri = Uri.parse(WEATHER_URL).buildUpon()
                .appendQueryParameter("id", MOSCOW_ID)
                .appendQueryParameter("appid", API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            new requestInternet().execute(url);
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
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    String getWeatherFromJson(String jsonStr) {
        try {
            JSONObject weatherObj = new JSONObject(jsonStr);
            JSONArray weather_predictions = weatherObj.getJSONArray("weather");
            String weather = weather_predictions.getJSONObject(0).getString ("main");
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
