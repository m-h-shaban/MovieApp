package com.example.dell.movieapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dell.movieapp.data.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dell on 6/20/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieData>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private MoviesAdapter mMoviesAdapter;
    private final Context mContext;

    public FetchMoviesTask(Context context, MoviesAdapter moviesAdapter) {
        mContext = context;
        mMoviesAdapter = moviesAdapter;
    }

    private ArrayList<MovieData> parseResult(String result) {
        ArrayList<MovieData> results = new ArrayList<MovieData>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonMovieObject = array.getJSONObject(i);
                MovieData movieData = new MovieData();

                movieData.setAdult(jsonMovieObject.getString("adult"));
                movieData.setBackdrop_path(jsonMovieObject.getString("backdrop_path"));
                movieData.setId(jsonMovieObject.getString("id"));
                movieData.setOriginal_language(jsonMovieObject.getString("original_language"));
                movieData.setOriginal_title(jsonMovieObject.getString("original_title"));
                movieData.setPopularity(jsonMovieObject.getString("popularity"));
                movieData.setVote_count(jsonMovieObject.getString("vote_count"));
                movieData.setVideo(jsonMovieObject.getString("video"));
                movieData.setOverview(jsonMovieObject.getString("overview"));
                movieData.setRelease_date(jsonMovieObject.getString("release_date"));
                movieData.setPoster_path(jsonMovieObject.getString("poster_path"));
                movieData.setVote_average(jsonMovieObject.getString("vote_average"));

                results.add(movieData);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected ArrayList<MovieData> doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {

            String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"?";

//                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
//                String order_by = sharedPref.getString("order_by", "");
//
//                if (order_by.equals("popular")) {
//                    FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
//                } else if (order_by.equals("top_rated")) {
//                    FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
//                }

            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "c258ef3167d2f4ec83da643c7f76b785")
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                Log.e("ERROR", "1");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                Log.e("ERROR", "2");
                return null;
            }
            forecastJsonStr = buffer.toString();
            return parseResult(forecastJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        Log.e("ERROR", "3");
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> data) {
        //super.onPostExecute(Data);
        if (data != null) {
            mMoviesAdapter.clear();
            for (MovieData movieData : data) {
                mMoviesAdapter.add(movieData);
            }
            mMoviesAdapter.notifyDataSetChanged();
            // New data is back from the server.  Hooray!
        }
    }
}