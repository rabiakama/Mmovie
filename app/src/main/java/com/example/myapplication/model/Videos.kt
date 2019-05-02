package com.example.myapplication.model

import com.google.gson.annotations.SerializedName


class Videos {

    @SerializedName("key")
    private var key: String? = null
    @SerializedName("name")
    //sonradan eklendi
    private var name:String?=null
   /* @SerializedName("id")
    var id: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("site")
    var site: String? = null
*/


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


