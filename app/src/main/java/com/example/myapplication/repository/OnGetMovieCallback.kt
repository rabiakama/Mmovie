package com.example.myapplication.repository

import com.example.myapplication.movie_detail.model.MovieDetail


interface OnGetMovieCallback {
    fun onSuccess(movie: MovieDetail)

    fun onError()

    
}