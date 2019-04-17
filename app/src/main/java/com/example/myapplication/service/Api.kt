package com.example.myapplication.service

import com.example.myapplication.model.GenresResponse
import com.example.myapplication.model.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    companion object {
        const val URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "534bc4143a626777d62c7d1ab8697aba"
        const val DEFAULT_LANGUAGE = "pt-BR"
        const val DEFAULT_REGION = "BR"
    }

    @GET("genre/movie/list")
    fun genres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<GenresResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Long,
        @Query("region") region: String
    ): Call<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Long,
        @Query("region") region: String
    ): Call<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Long,
        @Query("region") region: String
    ): Call<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Long,
        @Query("region") region: String
    ): Call<MoviesResponse>

    @GET("/account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page:Int
    ):Call<MoviesResponse>


    @GET("discover/movie")
    fun getMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("region") region: String,
        @Query("page") page:Int
    ): Call<MoviesResponse>


    @GET("movie/search")
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesResponse>

}