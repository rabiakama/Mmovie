package com.example.myapplication

import com.example.myapplication.model.Movies
import com.example.myapplication.model.MoviesResponse
import com.example.myapplication.repository.Repository
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito


class MovieDetailsTest{

    @Test
    fun getMoviesTitleNotEmpty(){
        //given
        val movies=Movies()

        //when
        val list=movies.getTitle()

        //then
        assertNotEquals(0,list?.length)

    }

    @Test
    fun getMoviesVoteAverageNotEmpty(){
        val movies=Movies()
        val list=movies.getVoteAverage()
        assertNotEquals(0,list?.toDouble())
    }

    @Test
    fun MoviesVoteAverage(){
        val respo:Repository?=null
        val movies=Movies()
        movies.setVoteAverage(5.5)

        val isSelectedMovie=respo?.getMovies()
       // assertTrue(isSelectedMovie)
    }


}