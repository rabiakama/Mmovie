package com.example.myapplication



import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import com.bumptech.glide.Glide
import com.example.myapplication.model.*
import com.example.myapplication.repository.OnGetMovieCallback
import com.example.myapplication.repository.Repository
import com.example.myapplication.service.Api
import com.example.myapplication.service.Client
import kotlinx.android.synthetic.main.activity_detail.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

import android.widget.Toast
import com.example.myapplication.adapter.TrailerAdapter
import com.example.myapplication.repository.FavHelper
import com.example.myapplication.repository.OnGetTrailersCallback
import org.json.JSONObject


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity() {


    private val BACK_DROP_URL = "https://image.tmdb.org/t/p/w500/"
    private val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var saveMovieRecordNumber: Int? = null
    private val SAVE_MOVIE_SUCCESS = 10
    private val SAVE_MOVIE_FAIL = 11
    private var movies: Movies?=null
    private var mtrailerList: ArrayList<Videos>?= arrayListOf()
    lateinit var mtrailerAdapter: TrailerAdapter
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
                    val favorite: Boolean? = null
                    if (favorite!!) {
                        addMovieToFavorite()
                    }

                }
            }
        }


    }


    private fun initView() {
        val adapter: TrailerAdapter? = null
        val mLayoutManager = LinearLayoutManager(applicationContext)
        detailsRv.layoutManager = mLayoutManager
        detailsRv.adapter = adapter
        adapter?.notifyDataSetChanged()
        //loadTrailer()

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
                loadTrailer()
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
    /*private fun loadTrailerss() {
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

    }*/

    private fun loadTrailer() {
        repository.getTrailers(movieID, object : OnGetTrailersCallback {
            override fun onSuccess(trailers: Array<Videos>?) {
                trailersLabel.visibility = View.VISIBLE
                movieTrailers.removeAllViews()
                for (trailer: Videos in trailers!!) {
                    val parent: View = layoutInflater.inflate(R.layout.videos_row, movieTrailers, false)
                    val thumbnail: ImageView = parent.findViewById(R.id.thumbnailVideo)
                    thumbnail.requestLayout()

                    thumbnail.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            showTrailer(YOUTUBE_VIDEO_URL+movies?.getId())

                        }

                    })

                    Glide.with(this@DetailActivity)
                        .load(YOUTUBE_THUMBNAIL_URL + trailer.getKey())
                        .placeholder(R.drawable.youtube)
                        .into(thumbnail)

                    movieTrailers.addView(parent)

                }
            }

            override fun onError() {
                trailersLabel.visibility = View.GONE
            }

        })
    }

    private fun showTrailer(url: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

        /*val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieID))
            startActivity(intent)*/
    }


    private fun populateTrailers(mMovieTrailers:ArrayList<Videos>){
        if(mMovieTrailers.size >0){
            mtrailerAdapter= TrailerAdapter(mMovieTrailers,object: TrailerAdapter.OnItemClickListener{
                override fun onItemClicked(trailer: Videos) {
                    val intent=Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=" +trailer.getKey()))
                    startActivity(intent)
                }
            })
            detailsRv.adapter=mtrailerAdapter

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addMovieToFavorite() {
        // val movieId=movies.getId()
        val sharedPreferences = getSharedPreferences(FavHelper.CONTENT_AUTHORITY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(movies?.getId().toString(), movieID)
        editor.apply()
        saveFavorite()

        Toast.makeText(this@DetailActivity, "Added to your favorite", Toast.LENGTH_SHORT).show()


    }


    private fun saveFavorite() {
        lateinit var favorites: Movies
        val values = ContentValues()
        values.put(FavHelper.COLUMN_TITLE, favorites.getTitle())
        values.put(FavHelper.COLUMN_POSTER_PATH, favorites.getPosterPath())
        values.put(FavHelper.COLUMN_PLOT_SYNOPSIS, favorites.getOverview())
        values.put(FavHelper.COLUMN_USERRATING, favorites.getVoteAverage())
        values.put(FavHelper.COLUMN_MOVIEID, favorites.getId())
        favoriteDbHelper?.addFavorite(favorites)

        val newUri: Uri? = contentResolver.insert(FavHelper.CONTENT_URI, values)

        if (newUri == null) {
            saveMovieRecordNumber = SAVE_MOVIE_FAIL
            Toast.makeText(this, "Movie Failed", Toast.LENGTH_SHORT).show()
        } else {
            saveMovieRecordNumber = SAVE_MOVIE_SUCCESS
            Toast.makeText(this, "Movie Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    //yeni eklenen
    private fun getNetworkınfo(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo=connectivityManager.activeNetworkInfo
        if(  activeNetworkInfo != null && activeNetworkInfo.isConnected){
            return true
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable("movie",movies)
        if(getNetworkınfo()){
            outState.putSerializable("movie_trailers",mtrailerList)

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

        favoriteDbHelper?.addFavorite(favorites)
        }
        */




