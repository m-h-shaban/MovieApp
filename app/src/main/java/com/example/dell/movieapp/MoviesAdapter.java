package com.example.dell.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.dell.movieapp.data.MovieData;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 6/19/2016.
 */
public class MoviesAdapter extends ArrayAdapter<MovieData>{

    public MoviesAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_movie, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+getItem(position).getPoster_path()).into(imageView);

        // add fetched image here

        return convertView;
    }
}
