package com.example.myapplication.model

 class VideosResponse {
     val results: List<Videos>? = null

      fun getYoutubeTrailer(): Videos? {
         results?.forEach { video ->
             if (video.type.equals(VideoTypes.Trailer.name) && video.site.equals("YouTube")) {
                 return video
             }
         }
         return null
     }
 }

