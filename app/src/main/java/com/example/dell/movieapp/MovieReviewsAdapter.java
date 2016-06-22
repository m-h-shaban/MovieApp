package com.example.dell.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell.movieapp.data.ReviewData;

/**
 * Created by dell on 6/20/2016.
 */
public class MovieReviewsAdapter extends ArrayAdapter<ReviewData> {


    public MovieReviewsAdapter(Context context) {
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
                    R.layout.list_item_review, parent, false);
        }

        TextView authorTextView = (TextView) convertView.findViewById(R.id.list_item_review_author);
        authorTextView.setText(getItem(position).getAuthor());

        TextView contentTextView = (TextView) convertView.findViewById(R.id.list_item_review_content);
        contentTextView.setText(getItem(position).getContent());

        // add fetched image here

        return convertView;
    }

}
