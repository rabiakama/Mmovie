package com.example.myapplication.repository



import com.example.myapplication.service.Api


class Repository(private val api: Api) {


    fun getPopularMovies()=api.getPopularMovies(apiKey =  "534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region="TR")

    //fun getNowPlayingMovies()=api.getPopularMovies(apiKey =  "534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region="TR")
    fun getMovies()= api.getMovie(apiKey = "534bc4143a626777d62c7d1ab8697aba",language = "en-US",region = "TR",sortBy = "original_title.desc",page=1)

    fun getDetails()=api.getDetails(apiKey = "534bc4143a626777d62c7d1ab8697aba",language = "en-US")
    fun getTopMovies() = api.getTopRatedMovies(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region = "TR")

    fun getComingMovies()=api.getUpcomingMovies(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US",page = 1,region = "TR")

    fun getGenres( )=api.genres(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US")

    fun getTrailers()=api.getTrailers(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "en-US")
}
//fun searchMovie() = api.searchMovie(apiKey ="534bc4143a626777d62c7d1ab8697aba",language = "pt-BR",page = 1)


                            //query:343611


