package com.example.myapplication.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.movie_detail.model.MovieDetail
import com.example.myapplication.videos.VideosResponse
import com.example.myapplication.data.remote.model.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository(private val api: Api) {

    fun getPopularMovies()=api.getPopularMovies(apiKey =  BuildConfig.ApiKey,language = "en-US",page = 1,region="TR")

    fun getNowPlayingMovies()=api.getNowPlayingMovies(apiKey =  BuildConfig.ApiKey,language = "en-US",page = 1,region="TR")
    fun getMovies()= api.getMovie(apiKey = BuildConfig.ApiKey,language = "en-US",region = "TR",sortBy = "original_title.desc",page=1)
    fun getTopMovies() = api.getTopRatedMovies(apiKey =BuildConfig.ApiKey,language = "en-US",page = 1,region = "TR")

    fun getTrailers(movieID: Int,callback: OnGetTrailersCallback){
        api.getTrailers(movieID,apiKey = BuildConfig.ApiKey)
            .enqueue(object :Callback<VideosResponse>{
                override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                    callback.onError()
                }

                override fun onResponse(call: Call<VideosResponse>, response: Response<VideosResponse>) {
                    if (response.isSuccessful){
                        val trailer: VideosResponse =response.body()!!
                        if(trailer.getTrailers() != null){
                           callback.onSuccess(trailer.getTrailers())
                        }else{
                            callback.onError()
                        }

                    }else{
                        callback.onError()
                    }
                }

            })
    }

  fun getMovieDetail(movieID:Int,callback:OnGetMovieCallback){

      api.getDetails(movieID,apiKey = BuildConfig.ApiKey)
              //language silindi,ekle
          .enqueue(object :Callback<MovieDetail>{
              override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                  callback.onError()
              }

              override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                  if (response.isSuccessful && response.body() != null) {
                      val movie: MovieDetail?=response.body()!!
                      if(movie!=null){
                          callback.onSuccess(movie)
                      }
                      else{
                          callback.onError()
                      }
                  }

                  }
          })
  }
}



