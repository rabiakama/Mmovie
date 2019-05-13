package com.example.myapplication.movie_detail.model

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.remote.model.Api
import com.example.myapplication.data.remote.retrofit.Client
import com.example.myapplication.movie_detail.view.DetailActivityContract
import com.example.myapplication.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailModel:DetailActivityContract.Model {

    private lateinit var repository: Repository

    override fun getMovieDetail(onFinishedListener: DetailActivityContract.Model.OnFinishedListener, movieId: Int) {

        val apiService:Api=Client.getClient()!!.create(Api::class.java)
        val call: Call<MovieDetail> = apiService.getDetails(movieId,BuildConfig.ApiKey)
        call.enqueue(object: Callback<MovieDetail>{

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                val movieDetail:MovieDetail=response.body()!!
                onFinishedListener.onFinished(movieDetail)

            }

        })
    }



}