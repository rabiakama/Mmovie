package com.example.myapplication.repository



import android.support.v7.widget.RecyclerView
import com.example.myapplication.model.MovieDetail
import com.example.myapplication.model.MoviesResponse
import com.example.myapplication.model.Videos
import com.example.myapplication.model.VideosResponse
import com.example.myapplication.service.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository(private val api: Api) {



    fun getPopularMovies()=api.getPopularMovies(apiKey =  "534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region="TR")

    //fun getNowPlayingMovies()=api.getPopularMovies(apiKey =  "534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region="TR")
    fun getMovies()= api.getMovie(apiKey = "534bc4143a626777d62c7d1ab8697aba",language = "en-US",region = "TR",sortBy = "original_title.desc",page=1)

    //fun getDetails(movieId: Int) = api.getDetails( apiKey = "534bc4143a626777d62c7d1ab8697aba",language = "en-US",movieId = movieId)
    fun getTopMovies() = api.getTopRatedMovies(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region = "TR")

    fun getComingMovies()=api.getUpcomingMovies(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region = "TR")

    fun getSearch( )=api.searchMovie(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US",query ="343611")

   // fun getTrailers()=api.getTrailers(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US")

    fun getTrailers(movieID: Int,callback: OnGetTrailersCallback){
        api.getTrailers(movieID,language = "en-Us",apiKey = "534bc4143a626777d62c7d1ab8697aba")
            .enqueue(object :Callback<VideosResponse>{
                override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                    callback.onError()
                }

                override fun onResponse(call: Call<VideosResponse>, response: Response<VideosResponse>) {
                    if (response.isSuccessful){
                        val trailer:VideosResponse=response.body()!!
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

      api.getDetails(movieID,language = "en-Us",apiKey = "534bc4143a626777d62c7d1ab8697aba")
          .enqueue(object :Callback<MovieDetail>{
              override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                  callback.onError()
              }

              override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                  if (response.isSuccessful && response.body() != null) {
                      val movie:MovieDetail?=response.body()!!
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
//fun searchMovie() = api.searchMovie(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "pt-BR",page = 1)


                            //query:343611


