package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.adapter.TrailerAdapter
import com.example.myapplication.model.Videos
import com.example.myapplication.model.VideosResponse
import com.example.myapplication.repository.OnGetTrailersCallback
import com.example.myapplication.repository.Repository
import com.example.myapplication.service.Api
import com.example.myapplication.service.Client
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_videos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosActivity : AppCompatActivity() {


    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var videoId: Int = 0
    private var adapterV: TrailerAdapter? = null
    private var trailerlist: ArrayList<Videos> = arrayListOf()

    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)
        repository = Repository(Client.getClient()!!.create(Api::class.java))

        videoId = intent.getIntExtra("MOVIE_ID", videoId)

        initView()

    }

    private fun initView() {
        val adapter: TrailerAdapter? = null
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recylerView_video.layoutManager = mLayoutManager
        recylerView_video.adapter = adapter
        adapter?.notifyDataSetChanged()
        loadTrailer()

    }


    private fun loadTrailer() {
        try {
            repository.getTrailerss().enqueue(object : Callback<VideosResponse> {
                override fun onResponse(call: Call<VideosResponse>, response: retrofit2.Response<VideosResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        var trailerAdapter:TrailerAdapter?=null
                        trailerlist.addAll(response.body()!!.results!!)
                        recylerView_video.layoutManager =
                            LinearLayoutManager(this@VideosActivity, LinearLayoutManager.VERTICAL, false)
                        trailerAdapter= TrailerAdapter(trailerlist)
                        recylerView_video.adapter = trailerAdapter
                        recylerView_video.smoothScrollToPosition(0)
                    }

                }

                override fun onFailure(call: retrofit2.Call<VideosResponse>, t: Throwable) {
                    Toast.makeText(this@VideosActivity, "Error", Toast.LENGTH_SHORT).show()
                    recylerView_video.visibility = View.INVISIBLE
                    recylerView_video.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }

    }
}