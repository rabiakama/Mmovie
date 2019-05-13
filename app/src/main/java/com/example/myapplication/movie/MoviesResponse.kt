package com.example.myapplication.movie

import com.google.gson.annotations.SerializedName


class MoviesResponse {
     @SerializedName("results")
     val results: ArrayList<Movies>?=null
 }
