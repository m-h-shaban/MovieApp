package com.example.dell.movieapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.dell.movieapp.data.MovieContract;
import com.example.dell.movieapp.data.MovieData;

import java.util.ArrayList;

/**
 * Created by dell on 6/19/2016.
 */
public class MainActivityFragment extends Fragment {

    ListView mListView;
    MoviesAdapter mMoviesAdapter;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(MovieData movieData);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask movieTask = new FetchMoviesTask(getContext(), mMoviesAdapter);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String order_by = sharedPref.getString(getString(R.string.pref_order_key), getString(R.string.pref_order_popular));
        if (order_by.equals("favorite")) {
            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            //Log.e("query", cursor.getCount() + "");

            ArrayList<MovieData> data = new ArrayList<>();

            int idIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);
            int originalTitleIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE);
            int overviewIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_OVERVIEW);
            int posterPathIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH);
            int releaseDateIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE);
            int voteAverageIndex = cursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE);

            while (cursor.moveToNext()) {
                MovieData movieData = new MovieData();

                movieData.setId(cursor.getString(idIndex));
                movieData.setOriginal_title(cursor.getString(originalTitleIndex));
                movieData.setOverview(cursor.getString(overviewIndex));
                movieData.setPoster_path(cursor.getString(posterPathIndex));
                movieData.setRelease_date(cursor.getString(releaseDateIndex));
                movieData.setVote_average(cursor.getString(voteAverageIndex));

                data.add(movieData);
            }

            mMoviesAdapter.clear();
            for (MovieData movieData : data) {
                mMoviesAdapter.add(movieData);
            }
            mMoviesAdapter.notifyDataSetChanged();

            cursor.close();

        } else {
            movieTask.execute(order_by);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMoviesAdapter = new MoviesAdapter(getActivity());

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mMoviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieData movieData = mMoviesAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movieData);
            }
        });

        return rootView;
    }


    //============================================================================


}
