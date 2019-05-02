package com.example.myapplication.model

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



    fun getName():String?{
        return name
    }
    fun setName(name:String){
        this.name=name
    }

    fun getKey():String?{
        return key
    }
    fun setKey(key:String){
        this.key=key
    }

    fun getSize():String?{
        return size
    }
    fun setSize(size:String){
        this.size=size
    }


    fun getTrailers(): Array<Videos>? {
        return results
    }

    fun setTrailers(trailers: Array<Videos>?) {
        this.results = trailers
    }
    fun getId(): Int? {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }



 }

