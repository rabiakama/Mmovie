package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

class VideosResponse {
    @SerializedName("results")
      var results: List<Videos>?=null
    @SerializedName("id")
     private var id:Int?=null

    fun getTrailers(): List<Videos>? {
        return results
    }

    fun setTrailers(trailers: List<Videos>?) {
        this.results = trailers
    }
    fun getId(): Int? {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }



 }

