package com.example.myapplication.repository


import com.example.myapplication.model.Videos

interface OnGetTrailersCallback {
    fun onSuccess(trailers: Array<Videos>?)

    fun onError()

}