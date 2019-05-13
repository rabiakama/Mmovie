package com.example.myapplication.videos


import com.google.gson.annotations.SerializedName

class VideosResponse {
    @SerializedName("results")
      var results: Array<Videos>?= arrayOf()
    @SerializedName("id")
     private var id:Int?=null
    @SerializedName("size")
    private var size:String?=null
    @SerializedName("key")
    private var key:String?=null
    @SerializedName("name")
    private var name:String?=null

    fun getTrailers(): Array<Videos>? {
        return results
    }

    fun setTrailers(trailers: Array<Videos>?) {
        this.results = trailers
    }




 }

