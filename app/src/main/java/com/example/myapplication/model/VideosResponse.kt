package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideosResponse {
    @SerializedName("results")
    @Expose
     private var trailers: List<Videos>?=null

    fun getTrailers(): List<Videos>? {
        return trailers
    }

    fun setTrailers(trailers: List<Videos>?) {
        this.trailers = trailers
    }

 }

