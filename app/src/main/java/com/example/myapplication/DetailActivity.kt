package com.example.myapplication


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.button.MaterialButton

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.myapplication.adapter.TrailerAdapter

import com.example.myapplication.model.*
import com.example.myapplication.repository.Repository
import com.example.myapplication.service.Api
import com.example.myapplication.service.Client
import com.example.myapplication.utils.ApiUtils
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.Request

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity(),MoviesAdapter.OnItemClickListener {

    companion object {
        var MOVIE_ID = "movie_id"
    }

    private val API_KEY = "534bc4143a626777d62c7d1ab8697aba"
    val EXTRA_MOVIE_ID = "MOVIE_ID"

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private lateinit var repository: Repository
    private var trailerList = ArrayList<Videos>()
    private var movielist = ArrayList<Movies>()
    private var videoadapter: TrailerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        repository = Repository(Client.getClient()!!.create(Api::class.java))
        //setToolBar()
        initCollapsingToolBar()
        initViews()
        val movie: Movies


        val intent: Intent = intent
        if (intent.hasExtra("movies")) {
            movie = getIntent().getParcelableExtra("movies")

            detail_title.setText(movie.getTitle())
            detailoverview.setText(movie.getOverview())
            vote_average.setText(movie.getVoteAverage().toString())
            releasedate.setText(movie.getReleaseDate())
            val posterBasePath = "https://image.tmdb.org/t/p/w500/"

            Glide.with(this)
                .load(posterBasePath + movie.getPosterPath())
                .placeholder(R.drawable.abc_ic_go_search_api_material)
                .into(details_image)


        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
        }

        favorite_button.setOnClickListener { object:View.OnClickListener {
            override fun onClick(v: View?) {

            }

        }

        }
    }


    /* private fun setToolBar() {
         setSupportActionBar(detailToolbar)

         if (supportActionBar != null) {
             supportActionBar!!.setDisplayHomeAsUpEnabled(true)
             supportActionBar!!.setDisplayShowTitleEnabled(false)

         }
     }*/




    private fun initCollapsingToolBar() {
        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsing_tool_bar)
        collapsingToolbar.title = " "
        val appbarLayout: AppBarLayout = findViewById(R.id.appbar)
        appbarLayout.setExpanded(true)
        appbarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow: Boolean = false
            var scrollRange: Int = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = getString(R.string.mocie_details)
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun initViews() {
        val trailerAdapter = TrailerAdapter(trailerList)

        val recyclerView: RecyclerView = findViewById(R.id.detailsRv)
        val lmanager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = lmanager
        recyclerView.adapter = trailerAdapter
        trailerAdapter.notifyDataSetChanged()
        loadJson()
    }

    override fun onItemClicked(movie: Movies) {
        Toast.makeText(this, "Detail Page", Toast.LENGTH_SHORT).show()
        //val intent=Intent(this,DetailActivity::class.java)
        //this.startActivity(intent)
    }

    private fun loadJson() {
        val Client = Client
        val apiService: Api = Client.getClient()!!.create(Api::class.java)
        val call: Call<VideosResponse> = apiService.getTrailers(MOVIE_ID, API_KEY)
        call.enqueue(object : Callback<VideosResponse> {
            override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<VideosResponse>, response: Response<VideosResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    trailerList.addAll(response.body()!!.results!!)
                    detailsRv.adapter = videoadapter
                    detailsRv.smoothScrollToPosition(0)
                }
            }

        })
    }

   private fun fetchCredits(){

   }
        private fun loadJson1() {

            try {
                repository.getDetails().enqueue(object : Callback<MoviesResponse> {
                    override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            movielist.addAll(response.body()!!.results!!)
                            detailsRv.adapter = videoadapter
                            detailsRv.smoothScrollToPosition(0)
                        }

                    }

                    override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                        Toast.makeText(this@DetailActivity, "Error", Toast.LENGTH_SHORT).show()
                        detailsRv.visibility = View.INVISIBLE
                        detailsRv.visibility = View.GONE
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}




