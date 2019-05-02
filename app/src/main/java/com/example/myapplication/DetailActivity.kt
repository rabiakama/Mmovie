package com.example.myapplication



import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import com.bumptech.glide.Glide
import com.example.myapplication.model.*
import com.example.myapplication.repository.OnGetMovieCallback
import com.example.myapplication.repository.Repository
import com.example.myapplication.service.Api
import com.example.myapplication.service.Client
import kotlinx.android.synthetic.main.activity_detail.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView

import android.widget.Toast
import com.example.myapplication.adapter.TrailerAdapter
import com.example.myapplication.repository.FavHelper
import com.example.myapplication.repository.OnGetTrailersCallback
import kotlinx.android.synthetic.main.videos_row.*
import retrofit2.Call
import retrofit2.Response


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity() {

    private val BACK_DROP_URL = "https://image.tmdb.org/t/p/w500/"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var saveMovieRecordNumber: Int?=null
    private val SAVE_MOVIE_SUCCESS = 10
    private val SAVE_MOVIE_FAIL = 11
    private var movies:Movies?=null
    private var movieID: Int = 0
    var MOVIE_ID = "movie_id"

    private val favoriteDbHelper: FavHelper? = null



    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        movieID = intent.getIntExtra("MOVIE_ID", movieID)
        repository = Repository(Client.getClient()!!.create(Api::class.java))

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        // supportActionBar?.title=movie.getTitle()
        loadDetail()
        //loadTrailer()
        initCollapsingToolbar()


        /* favorite_button.setOnClickListener {
            val intent=Intent(this,FavoriteActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Added to Your Favorite Page",Toast.LENGTH_LONG).show()
        }*/


        favorite_button.setOnClickListener {
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    val favorite: Boolean = true

                    if (favorite) {
                        val editor = getSharedPreferences( FavHelper.CONTENT_AUTHORITY,
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putBoolean("Favorite Added", true)
                        editor.apply()//neden commit yerine kullanılıyor
                            saveFavorite()

                        Toast.makeText(this@DetailActivity, "Added to your favorite", Toast.LENGTH_SHORT).show()

                    } else {
                        val movie_id = intent.extras.getInt("id")
                        favoriteDbHelper?.deleteFavorite(movie_id)
                        val editor =
                            getSharedPreferences(
                                FavHelper.CONTENT_AUTHORITY,
                                Context.MODE_PRIVATE
                            ).edit()
                        editor.putBoolean("Favorite Removed", true)
                        editor.apply()
                            saveFavorite()
                        Toast.makeText(this@DetailActivity, "Removed to your favorite", Toast.LENGTH_SHORT).show()
                    }


                }
            }
            initView()


        }
        video_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val trailer: Videos? = null
                Toast.makeText(this@DetailActivity, "Trailer Page", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@DetailActivity, VideosActivity::class.java)
                intent.putExtra("VIDEO_ID", trailer?.getKey())
                this@DetailActivity.startActivity(intent)
            }
        })

    }

    private fun initView() {
        val adapter: MoviesAdapter? = null
        val mLayoutManager = LinearLayoutManager(applicationContext)
        detailsRv.layoutManager = mLayoutManager
        detailsRv.adapter = adapter
        adapter?.notifyDataSetChanged()
        loadTrailerss()

    }

    private fun initCollapsingToolbar() {
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


    private fun loadDetail() {

        repository.getMovieDetail(movieID, object : OnGetMovieCallback {
            override fun onSuccess(movie: MovieDetail) {

                detail_title.text = movie.getTitle()
                releasedate.text = movie.getReleaseDate()
                vote_average.text = movie.getVoteAverage()!!.toDouble().toString()
                original_language.text = movie.getOriginalLanguage()
                detailoverview.text = movie.getOverview()
                //loadTrailerss()
                if (!isFinishing) {
                    Glide.with(this@DetailActivity)
                        .load(BACK_DROP_URL + movie.getPosterPath())
                        .placeholder(R.drawable.abc_ic_go_search_api_material)
                        .into(details_image)

                }
            }

            override fun onError() {
                finish()
            }

        })

    }

    private fun loadTrailerss() {
        repository.getTrailerss().enqueue(object : retrofit2.Callback<VideosResponse> {
            override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<VideosResponse>, response: Response<VideosResponse>) {
                val list: ArrayList<Videos> = arrayListOf()
                val trailerAdapter: TrailerAdapter? = null
                list.clear()
                list.addAll(response.body()!!.results!!)
                detailsRv.adapter = trailerAdapter
                detailsRv.smoothScrollToPosition(0)
            }

        })

    }

    private fun loadTrailer() {
        repository.getTrailers(movieID, object : OnGetTrailersCallback {
            override fun onSuccess(trailers: List<Videos>?) {
                trailersLabel.visibility = View.VISIBLE
                movieTrailers.removeAllViews()
                for (trailer: Videos in trailers!!) {
                    val parent: View = layoutInflater.inflate(R.layout.videos_row, movieTrailers, false)
                    val thumbnail: ImageView = parent.findViewById(R.id.thumbnailVideo)
                    thumbnail.requestLayout()

                    thumbnail.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            showTrailer(YOUTUBE_VIDEO_URL, trailer.getKey())

                        }

                    })
                    Glide.with(this@DetailActivity)
                        .load(YOUTUBE_THUMBNAIL_URL + trailer.getKey())
                        .placeholder(R.drawable.abc_ic_go_search_api_material)
                        .into(thumbnail)

                    movieTrailers.addView(parent)

                }
            }

            override fun onError() {
                trailersLabel.visibility = View.GONE
            }

        })
    }

    private fun showTrailer(url: String, key: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieID))
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveFavorite(){
        lateinit var favorites:Movies
        // Create a ContentValues object where column names are the keys, and current movie
        // attributes are the values.
        val values = ContentValues()
        values.put(FavHelper.COLUMN_TITLE, favorites.getTitle())
        values.put(FavHelper.COLUMN_POSTER_PATH, favorites.getPosterPath())
        values.put(FavHelper.COLUMN_PLOT_SYNOPSIS, favorites.getOverview())
        values.put(FavHelper.COLUMN_USERRATING, favorites.getVoteAverage())
        values.put(FavHelper.COLUMN_MOVIEID, favorites.getId())
        favoriteDbHelper?.addFavorite(favorites)

        val newUri: Uri?  = contentResolver.insert(FavHelper.CONTENT_URI, values)

        if (newUri == null) {
            saveMovieRecordNumber = SAVE_MOVIE_FAIL
            Toast.makeText(this,"Movie Failed",Toast.LENGTH_SHORT).show()
        } else {
            saveMovieRecordNumber = SAVE_MOVIE_SUCCESS
            Toast.makeText(this,"Movie Successfully",Toast.LENGTH_SHORT).show()
        }
    }





}


   /* @RequiresApi(Build.VERSION_CODES.P)
    fun saveFavorite(){
       lateinit var favorites:Movies
        val title:String=""
        val poster:String=""
        val overvieW:String=""

        val movies:Movies?=null
        val rate: Double? = movies?.getVoteAverage()

        favorites.setId(movieID)
        favorites.setTitle(title)
        favorites.setPosterPath(poster)
        favorites.setVoteAverage(rate)
        favorites.setOverview(overvieW)

        favoriteDbHelper?.addFavorite(favorites)*/



