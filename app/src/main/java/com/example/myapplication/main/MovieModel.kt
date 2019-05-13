package com.example.myapplication.main

import android.util.Log
import com.example.myapplication.data.remote.model.Api
import com.example.myapplication.data.remote.retrofit.Client
import com.example.myapplication.movie.Movies
import com.example.myapplication.movie.MoviesResponse
import com.example.myapplication.repository.Repository
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MovieModel : MainActivityContract.Model {

    lateinit var repository:Repository


    override fun getMovieList(onFinishedListener: MainActivityContract.Model.OnFinishedListener, pageNo: Long, language: String, region: String){
        repository.getMovies().enqueue(object :retrofit2.Callback<MoviesResponse>{
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                onFinishedListener.onFailure(t)

            }

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
               val movies:List<Movies> = response.body()?.results!!
                onFinishedListener.onFinished(movies)

            }

        })


    }
}