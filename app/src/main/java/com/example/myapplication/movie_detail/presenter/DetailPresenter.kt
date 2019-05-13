package com.example.myapplication.movie_detail.presenter


import com.example.myapplication.movie.Movies
import com.example.myapplication.movie_detail.model.MovieDetail
import com.example.myapplication.movie_detail.view.DetailActivityContract


class DetailPresenter:DetailActivityContract.Presenter,DetailActivityContract.Model.OnFinishedListener {


    private val movieDetailView: DetailActivityContract.View? = null
    private val movieDetailsModel: DetailActivityContract.Model? = null



    override fun onFinished(movie: MovieDetail) {

        if (movieDetailView != null) {
            movieDetailView.setDataToViews(movie)
        }
    }

    override fun onFailure(t: Throwable) {

        if (movieDetailView != null) {
            movieDetailView.onResponseFailure(t)
        }
    }

    override fun requestMovieData(movieId: Int) {
        if (movieDetailsModel != null) {
            movieDetailsModel.getMovieDetail(this,movieId)
        }

    }


}