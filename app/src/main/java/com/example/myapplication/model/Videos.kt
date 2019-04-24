package com.example.myapplication.model

import com.google.gson.annotations.SerializedName


class Videos {
    @SerializedName("key")
    private var key: String? = null
    @SerializedName("name")
    private var name:String?=null

    fun getKey(): String? {
        return key
    }

    fun setKey(key: String?) {
        this.key = key
    }

    fun getName(): String? {
        return key
    }

    fun setName(key: String?) {
        this.key = key
    }

}


