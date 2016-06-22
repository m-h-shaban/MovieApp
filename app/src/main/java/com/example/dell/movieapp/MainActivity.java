package com.example.dell.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dell.movieapp.data.MovieData;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_activity_container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                //Log.e("ggggg1", "asd");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_activity_container, new DetailFragment(), "DFTAG")
                        .commit();
            }

        } else {
            mTwoPane = false;
        }

        //Log.e("ggg", mTwoPane + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieData movieData) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putSerializable("MovieData", movieData);

            //Log.e("movie", movieData + "");

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_activity_container, fragment, "DFTAG")
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("MovieData", movieData);
            startActivity(intent);
        }
    }
}
