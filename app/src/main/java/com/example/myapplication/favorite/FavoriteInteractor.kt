package com.example.myapplication.favorite

import com.example.myapplication.movie.Movies

interface FavoriteInteractor {


    fun saveFavorite(movie: Movies)

    fun unFavorite(id: Int)
}