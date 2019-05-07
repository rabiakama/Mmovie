package com.example.myapplication.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import android.content.ContentValues
import android.net.Uri

import com.example.myapplication.model.Movies





class FavHelper(context: Context, name: String?,
                factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    var dbhandler: SQLiteOpenHelper? = null
    var db: SQLiteDatabase? = null

    companion object {

        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "favorite.db"
        val TABLE_PRODUCTS = "FAVORITES"

        val _ID = "_id"
        val TABLE_NAME = "favorite"
        val COLUMN_TITLE = "title"
        val COLUMN_RELEASE_DATE ="release_date"
        val COLUMN_MOVIEID = "movie_id"
        val COLUMN_USERRATING = "vote_average"
        val COLUMN_POSTER_PATH = "poster_path"
        //val COLUMN_PLOT_SYNOPSIS = "overview"
        val CONTENT_AUTHORITY = "com.example.myapplication.repository"
        val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)
        val  CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(TABLE_NAME)
            .build()



    }


    fun open() {
        Log.i(TABLE_PRODUCTS, "Database Opened")
        db = dbhandler?.writableDatabase
    }

    override fun close() {
        Log.i(TABLE_PRODUCTS, "Database Closed")
        dbhandler?.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MOVIEID + " INTEGER, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                COLUMN_USERRATING + " REAL NOT NULL, " +
                COLUMN_POSTER_PATH + " TEXT NOT NULL " +
                //COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL" +
                ");"
        db.execSQL(SQL_CREATE_FAVORITE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME)
        onCreate(db)
    }

    fun addFavorite(movie: Movies) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_MOVIEID, movie.getId())
        values.put(COLUMN_TITLE, movie.getOriginalTitle())
        values.put(COLUMN_RELEASE_DATE,movie.getReleaseDate())
        values.put(COLUMN_USERRATING, movie.getVoteAverage().toString())
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath())
       // values.put(COLUMN_PLOT_SYNOPSIS, movie.getOverview())

        db.insert(TABLE_NAME, "", values)
        db.close()
    }


    fun deleteFavorite(id: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_NAME,
            COLUMN_MOVIEID + "=" + id,
            null
        )
    }

    fun getAllFavorite(): ArrayList<Movies> {
        val columns = arrayOf(
            _ID,
            COLUMN_MOVIEID,
            COLUMN_TITLE,
            COLUMN_RELEASE_DATE,
            COLUMN_USERRATING,
            COLUMN_POSTER_PATH
           // COLUMN_PLOT_SYNOPSIS
        )
        val sortOrder: String = _ID + " " +"ASC"
        val favoriteList: ArrayList<Movies> = arrayListOf()

        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            columns,
            null, null, null, null,
            sortOrder
        )
        if (cursor.moveToFirst()) {
            do {
                val movie= Movies()
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIEID))))
                movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)))
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)))
                movie.setVoteAverage((cursor.getDouble(cursor.getColumnIndex(COLUMN_USERRATING))))
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)))
               // movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_PLOT_SYNOPSIS)))

                favoriteList.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return favoriteList

    }
}