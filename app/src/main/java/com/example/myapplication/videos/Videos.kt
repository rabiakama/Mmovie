package com.example.myapplication.videos

import com.google.gson.annotations.SerializedName


class Videos {

    @SerializedName("key")
    private var key: String? = null
    @SerializedName("name")
    //sonradan eklendi
    private var name:String?=null


    fun getName(): String? {
        return key
    }

    fun setName(key: String?) {
        this.key = key
    }

    fun getKey(): String? {
        return key
    }

    fun setKey(key: String?) {
        this.key = key
    }

}


