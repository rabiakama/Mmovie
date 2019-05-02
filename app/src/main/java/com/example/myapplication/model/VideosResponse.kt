package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

class VideosResponse {
    @SerializedName("results")
      var results: List<Videos>?=null

    fun getTrailers(): List<Videos>? {
        return results
    }

    fun setTrailers(trailers: List<Videos>?) {
        this.results = trailers
    }

 }

