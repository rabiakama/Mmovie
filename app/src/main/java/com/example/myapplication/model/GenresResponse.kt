package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

class GenresResponse {
  @SerializedName("genres")
  var genres:ArrayList<Genres>?=null

  fun getGenres():List<Genres>?{
    return genres
  }



}

