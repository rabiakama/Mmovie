package com.example.myapplication.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.model.Movies
import com.example.myapplication.model.MoviesResponse
import com.example.myapplication.service.Api
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import retrofit2.Response
import rx.Single


@RunWith(JUnit4::class)
class RepositoryTest {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var apiService: Api
    lateinit var movies: Movies
    lateinit var movieRepository: Repository



    @Before
    fun setup(){
        movieRepository=Repository(apiService)
    }

    @Test
    fun popularMovies(){
        movies.setTitle("null")
        movies.setId(0)
        movies.setVoteAverage(5.0)
        movies.setReleaseDate("2009")
    }





}