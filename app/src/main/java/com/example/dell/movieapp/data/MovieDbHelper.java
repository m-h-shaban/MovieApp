package com.example.dell.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dell.movieapp.data.MovieContract.*;

/**
 * Created by dell on 6/20/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

     static final String DATABASE_NAME = "movie.db";
     static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }

}
