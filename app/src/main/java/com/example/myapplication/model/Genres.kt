package com.example.myapplication.model

import android.R.id
import com.google.gson.annotations.SerializedName


class Genres {

  @SerializedName("id")
  private var id: Int? = null
  @SerializedName("name")
  private var name: String? = null

  fun getId(): Int? {
    return id
  }

  fun setId(id: Int?) {
    this.id = id
  }

  fun getName(): String? {
    return name
  }

  fun setName(name: String?) {
    this.name = name
  }
}