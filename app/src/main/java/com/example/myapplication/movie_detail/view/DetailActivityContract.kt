package com.example.myapplication.movie_detail.view

import com.example.myapplication.movie.Movies
import com.example.myapplication.movie_detail.model.MovieDetail


interface DetailActivityContract {


    interface Model{

        interface OnFinishedListener {
            fun onFinished(movie: MovieDetail)

            fun onFailure(t: Throwable)
        }


        fun getMovieDetail(onFinishedListener:OnFinishedListener,movieId:Int)

    }

    interface View{

        fun setDataToViews(movie:MovieDetail)
        fun onResponseFailure(t:Throwable)

    }

    interface Presenter{
        fun requestMovieData(movieId:Int)


    }
}