package com.example.myapplication.model

enum class VideoTypes {
    Trailer, Teaser, Clip, Featurette
}

data class Videos (
    val key: String? = null,
    val type: String? = null,
    val site: String? = null
)