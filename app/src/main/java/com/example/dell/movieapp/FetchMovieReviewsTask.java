package com.example.dell.movieapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.dell.movieapp.data.ReviewData;

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
public class FetchMovieReviewsTask extends AsyncTask<String, Void, ArrayList<ReviewData>> {

    private final String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();

    private MovieReviewsAdapter mMoviesReviewsAdapter;
    private final Context mContext;
    ListView mReviewsListView;

    public FetchMovieReviewsTask(Context context, MovieReviewsAdapter moviesReviewsAdapter,ListView reviewsListView) {
        mContext = context;
        mMoviesReviewsAdapter = moviesReviewsAdapter;
        mReviewsListView = reviewsListView;
    }

    private ArrayList<ReviewData> parseResult(String result) {
        ArrayList<ReviewData> results = new ArrayList<ReviewData>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonMovieObject = array.getJSONObject(i);
                ReviewData reviewData = new ReviewData();

                reviewData.setId(jsonMovieObject.getString("id"));
                reviewData.setAuthor(jsonMovieObject.getString("author"));
                reviewData.setUrl(jsonMovieObject.getString("url"));
                reviewData.setContent(jsonMovieObject.getString("content"));

                results.add(reviewData);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected ArrayList<ReviewData> doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {

            // http://api.themoviedb.org/3/movie/269149/reviews?api_key=c258ef3167d2f4ec83da643c7f76b785

            String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"/reviews?";

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
    protected void onPostExecute(ArrayList<ReviewData> data) {
        //super.onPostExecute(Data);
        if (data != null) {
            mMoviesReviewsAdapter.clear();
            for (ReviewData reviewData : data) {
                mMoviesReviewsAdapter.add(reviewData);
            }
            mMoviesReviewsAdapter.notifyDataSetChanged();
            justifyListViewHeightBasedOnChildren(mReviewsListView);
            // New data is back from the server.  Hooray!
        }
    }

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}