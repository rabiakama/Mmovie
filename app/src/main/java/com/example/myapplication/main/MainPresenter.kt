package com.example.myapplication.main

import com.example.myapplication.data.remote.model.Api
import com.example.myapplication.movie.Movies
import com.example.myapplication.repository.Repository



class MainPresenter : MainActivityContract.Presenter,MainActivityContract.Model.OnFinishedListener {
    override fun onFinished(movieArrayList: List<Movies>) {

    }

    override fun onFailure(t: Throwable) {

    }

    lateinit var movieApi: Api
    lateinit var repository: Repository

    override fun loadMovies() {
        repository.getMovies()


    }

    private lateinit var mView:MainActivityContract.View


    override fun setView(view: MainActivityContract.View) {
        this.mView=view
    }


}