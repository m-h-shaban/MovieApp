package com.example.dell.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dell on 6/19/2016.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            DetailFragment fragment = new DetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_activity_container, fragment)
                    .commit();
        }
    }

}
