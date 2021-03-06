package com.projects.jezinka.odjazd_client;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ODJAZD_SERVER_URL = "https://odjazd-189315.appspot.com/";
    private static final String PARAM_FROM = "from";
    private static final String WORK_PARAM = "work";
    private static final String HOME_PARAM = "home";

    private RecyclerView mRoutesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoutesList = findViewById(R.id.route_list_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRoutesList.setLayoutManager(layoutManager);
        mRoutesList.setHasFixedSize(true);
    }

    public void homeButtonClick(View view) {
        URL homeUrl = createUrl(HOME_PARAM);
        new OdjazdQueryTask().execute(homeUrl);
    }

    public void workButtonClick(View view) {

        URL workUrl = createUrl(WORK_PARAM);
        new OdjazdQueryTask().execute(workUrl);
    }

    private URL createUrl(String param) {
        Uri builtUri = Uri.parse(ODJAZD_SERVER_URL).buildUpon().appendQueryParameter(PARAM_FROM, param).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
        return url;
    }

    private String makeOdjazdQuery(URL url) {

        if (url != null) {
            try {
                return getResponseFromHttpUrl(url);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
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

    private class OdjazdQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONArray scheduleJsonList = new JSONArray(s);
                    RoutesAdapter mAdapter = new RoutesAdapter(scheduleJsonList);
                    mRoutesList.setAdapter(mAdapter);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            return makeOdjazdQuery(url);
        }
    }
}
