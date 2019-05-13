package com.example.myapplication.favorite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.data.local.FavHelper
import com.example.myapplication.movie.Movies




class FavoriteInteractorImpl :FavoriteInteractor {
    override fun unFavorite(id: Int) {
        favoriteDbHelper?.deleteFavorite(id)
    }

    private  var favoriteDbHelper: FavHelper?=null
    private val context:Context?=null


    override fun saveFavorite(movie: Movies) {
        val factory: SQLiteDatabase.CursorFactory? = null

        favoriteDbHelper = context?.let { FavHelper(it, "favorite.db", factory, 2) }

        favoriteDbHelper?.addFavorite(movie)


    }

}