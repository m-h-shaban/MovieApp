package com.example.dell.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.movieapp.data.MovieContract;
import com.example.dell.movieapp.data.MovieData;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 6/19/2016.
 */
public class DetailFragment extends Fragment {

    MovieReviewsAdapter mMovieReviewsAdapter;
    MovieTrailersAdapter mMovieTrailersAdapter;
    MovieData mMovieData;


    @Override
    public void onStart() {
        super.onStart();
        updateMoviesReviewsAndTrailers();
    }

    private void updateMoviesReviewsAndTrailers() {

        if (mMovieData != null) {
            FetchMovieTrailersTask fetchMovieTrailersTask = new FetchMovieTrailersTask(getContext(), mMovieTrailersAdapter, trailersListView);
            fetchMovieTrailersTask.execute(mMovieData.getId());

            FetchMovieReviewsTask fetchMovieReviewsTask = new FetchMovieReviewsTask(getContext(), mMovieReviewsAdapter, reviewsListView);
            fetchMovieReviewsTask.execute(mMovieData.getId());
        }
    }


    public void addOrRemoveFromFavorites() {

        Cursor cursor = getContext().getContentResolver().query(
                MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(mMovieData.getId()).build(),
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            getContext().getContentResolver().delete(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(mMovieData.getId())}
            );

            favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unset_fav, 0, 0, 0);

        } else {
            ContentValues MovieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            MovieValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, mMovieData.getId());
            MovieValues.put(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, mMovieData.getOriginal_title());
            MovieValues.put(MovieContract.FavoriteEntry.COLUMN_OVERVIEW, mMovieData.getOverview());
            MovieValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH, mMovieData.getPoster_path());
            MovieValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, mMovieData.getRelease_date());
            MovieValues.put(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, mMovieData.getVote_average());

            Uri insertedUri = getContext().getContentResolver().insert(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieValues
            );
            favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.set_fav, 0, 0, 0);
            //Log.e("insert ", ContentUris.parseId(insertedUri)+"");
        }

        cursor.close();
    }

    Button favButton;
    TextView releaseDate;
    TextView overView;
    TextView voteAverage;
    TextView originalTitle;
//    TextView trailersHeader;
//    TextView reviewsHeader;
    ImageView poster;
    ListView reviewsListView;
    ListView trailersListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        mMovieData = (MovieData) intent.getSerializableExtra("MovieData");

        //Log.e("intent", mMovieData+"");

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovieData = (MovieData) arguments.getSerializable("MovieData");
        }

        //Log.e("bundle", mMovieData+"");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        originalTitle = (TextView) rootView.findViewById(R.id.original_title);
        poster = (ImageView) rootView.findViewById(R.id.movie_image_small);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        overView = (TextView) rootView.findViewById(R.id.overview);
        voteAverage = (TextView) rootView.findViewById(R.id.user_rating);
//        trailersHeader = (TextView) rootView.findViewById(R.id.trailers);
//        reviewsHeader = (TextView) rootView.findViewById(R.id.reviews);
        favButton = (Button) rootView.findViewById(R.id.mark_as_fav);

        mMovieReviewsAdapter = new MovieReviewsAdapter(getActivity());

        // Get a reference to the ListView, and attach this adapter to it.
        reviewsListView = (ListView) rootView.findViewById(R.id.listview_reviews);
        reviewsListView.setAdapter(mMovieReviewsAdapter);

        reviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mMovieReviewsAdapter.getItem(position).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {

                }

            }
        });


        mMovieTrailersAdapter = new MovieTrailersAdapter(getActivity());

        // Get a reference to the ListView, and attach this adapter to it.
        trailersListView = (ListView) rootView.findViewById(R.id.listview_trailers);
        trailersListView.setAdapter(mMovieTrailersAdapter);

        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String video_path = "http://www.youtube.com/watch?v=" + mMovieTrailersAdapter.getItem(position).getKey();

                Uri uri = Uri.parse(video_path);

                // With this line the Youtube application, if installed, will launch immediately.
                // Without it you will be prompted with a list of the application to choose.
                uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {

                    }
                }

            }
        });


        if (mMovieData != null) {
            voteAverage.setText(mMovieData.getVote_average() + "/10");
            overView.setText(mMovieData.getOverview());
            releaseDate.setText(mMovieData.getRelease_date());
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + mMovieData.getPoster_path()).into(poster);
            originalTitle.setText(mMovieData.getOriginal_title());

            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(mMovieData.getId()).build(),
                    null,
                    null,
                    null,
                    null
            );

            if (cursor.getCount() > 0) {
                favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.set_fav, 0, 0, 0);
            }
//            else{
//                favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unset_fav, 0, 0, 0);
//            }
//
//            favButton.setText("FAVORITE");
//            trailersHeader.setText("Trailers");
//            trailersHeader.setText("Reviews");

            cursor.close();

            favButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addOrRemoveFromFavorites();
                }
            });

        }else{
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.fragment_detail_container);
            linearLayout.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }


}
