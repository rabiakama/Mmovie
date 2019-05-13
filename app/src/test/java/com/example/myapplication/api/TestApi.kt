package com.example.myapplication.api

import com.example.asus.a03042019.utils.getBackdropUrl
import com.example.myapplication.movie.Movies
import com.example.myapplication.data.remote.model.Api
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito


class TestApi {

    private lateinit var apiService: Api

    @Before
    fun initService(){
        this.apiService= Mockito.mock<Api>(Api::class.java)

    }

    @Test
    fun getMovieDetails() {
        val movies = Movies()
        apiService.getDetails(1,"","en-Us")
        Assert.assertEquals("Burning",movies.getTitle())
        Assert.assertEquals(getBackdropUrl("/fki3kBlwJzFp8QohL43g9ReV455.jpg"),movies.getBackdropPath())

    }



}
