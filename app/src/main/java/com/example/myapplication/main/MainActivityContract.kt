package com.example.myapplication.main

import com.example.myapplication.movie.Movies

interface MainActivityContract {


    interface Model{
        interface OnFinishedListener {
            fun onFinished (movieArrayList:List<Movies>)

            fun onFailure( t:Throwable)
        }

        fun getMovieList( onFinishedListener:OnFinishedListener, pageNo:Long,language:String,region:String)
    }


    interface View{
        fun showError(value: String)

    }

    interface Presenter{

        fun setView(view:View)
        fun loadMovies()



    }
}