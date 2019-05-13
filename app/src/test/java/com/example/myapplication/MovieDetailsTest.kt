package com.example.myapplication

import com.example.myapplication.movie.Movies
import org.junit.Assert.*
import org.junit.Test


class MovieDetailsTest{

    @Test
    fun getMoviesTitleNotEmpty(){
        //given
        val movies= Movies()

        //when
        val list=movies.getTitle()

        //then
        assertNotEquals(0,list?.length)

    }

    @Test
    fun getMoviesVoteAverageNotEmpty(){
        val movies= Movies()
        val list=movies.getVoteAverage()
        assertNotEquals(0,list?.toDouble())
    }


    @Test
    fun saveFavorite(){
        val favorites = Movies()


    }


}