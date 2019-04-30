package com.example.myapplication.repository

import com.example.myapplication.model.MovieDetail
import com.example.myapplication.model.Movies


interface OnGetMovieCallback {
    fun onSuccess(movie: MovieDetail)

    fun onError()

    
}