package com.example.dell.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell.movieapp.data.TrailerData;

/**
 * Created by dell on 6/20/2016.
 */
public class MovieTrailersAdapter extends ArrayAdapter<TrailerData> {

    public MovieTrailersAdapter(Context context) {
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
                    R.layout.list_item_trailers, parent, false);
        }

        TextView trailerNumberTextView = (TextView) convertView.findViewById(R.id.list_item_trailer_number);
        trailerNumberTextView.setText("Trailer "+(position+1));

        // add fetched image here

        return convertView;
    }

}
