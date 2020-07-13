package com.example.ac.nightmovieapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AC on 10/21/2016.
 */

public class Async_task extends AsyncTask<String,Void,Movie[]> {

    private static final String LOG_TAG = Async_task.class.getSimpleName();

    @Override
    protected Movie[] doInBackground(String... params) {
        String pop_or_top = params[0];
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonstr = null;

        String jsonstor = null;
        try {
            url = new URL("http://api.themoviedb.org/3/movie"+pop_or_top+"?api_key="+BuildConfig.API_KEY);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer jsonhere = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonhere.append(line);
            }

            jsonstor = jsonhere.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            }
        Movie[] present;
        {
            present = new Movie[20];
            try {
                JSONObject jobjects = new JSONObject(jsonstor);
                JSONArray jMovies = jobjects.getJSONArray("results");
                for (int i = 0; i < jMovies.length(); i++) {
                    JSONObject build = jMovies.getJSONObject(i);
                    present[i] = new Movie(build.getString("id"), build.getString("poster_path"));
                    present[i].setDate(build.getString("release_date"));
                    present[i].setTitle(build.getString("title"));
                    present[i].setReview(build.getString("vote_average"));
                    present[i].setSummary(build.getString("overview"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, "posterURI:error");
            }
            Log.v(LOG_TAG, "posterURI:done");
        }
            return present;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
    }
}
