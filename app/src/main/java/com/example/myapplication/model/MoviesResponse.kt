package com.example.myapplication.model

import com.google.gson.annotations.SerializedName


class MoviesResponse {
     @SerializedName("results")
     val results: ArrayList<Movies>?=null
 }
