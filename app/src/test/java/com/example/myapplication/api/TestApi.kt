package com.example.myapplication.api

import com.example.asus.a03042019.utils.getBackdropUrl
import com.example.myapplication.model.Movies
import com.example.myapplication.model.MoviesResponse
import com.example.myapplication.service.Api
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito
import java.lang.Exception


class TestApi {

    private lateinit var apiService:Api
    private  var IMAGE_URL=""


    @Before
    fun initService(){
        this.apiService= Mockito.mock<Api>(Api::class.java)

    }

    @Test
    fun fetchMovieDetails() {
        val movies =Movies()
        apiService.getDetails(1,"","en-Us")
        Assert.assertEquals("Burning",movies.getTitle())
        Assert.assertEquals(getBackdropUrl("/fki3kBlwJzFp8QohL43g9ReV455.jpg"),movies.getBackdropPath())

    }



}
