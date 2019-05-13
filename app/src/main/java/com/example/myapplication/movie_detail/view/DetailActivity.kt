package com.example.myapplication.movie_detail.view



import android.content.*
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.Snackbar
import com.bumptech.glide.Glide
import com.example.myapplication.repository.OnGetMovieCallback
import com.example.myapplication.repository.Repository
import com.example.myapplication.data.remote.model.Api
import com.example.myapplication.data.remote.retrofit.Client
import kotlinx.android.synthetic.main.activity_detail.*
import com.example.myapplication.movie.Movies
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.videos.Videos
import com.example.myapplication.data.local.FavHelper
import com.example.myapplication.movie_detail.model.MovieDetail
import com.example.myapplication.movie_detail.presenter.DetailPresenter
import com.example.myapplication.repository.OnGetTrailersCallback



@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity(), DetailActivityContract.View {

    private val detailPresenter: DetailPresenter?=null


    private val BACK_DROP_URL = "https://image.tmdb.org/t/p/w500/"
    private val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private  var movies: Movies?=null
    private var mtrailerList: ArrayList<Videos>?= arrayListOf()
    private var movieID: Int = 0
    var MOVIE_ID = "movie_id"
    val factory: SQLiteDatabase.CursorFactory? = null

    private  var favoriteDbHelper: FavHelper?=null


    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        movieID = intent.getIntExtra("MOVIE_ID", movieID)
        repository = Repository(Client.getClient()!!.create(Api::class.java))
        favoriteDbHelper = FavHelper(this, "favorite.db", factory, 2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        //loadDetail()
        initCollapsingToolbar()
        if (detailPresenter != null) {
            detailPresenter.requestMovieData(movieID)
        }



        favorite_button.setOnClickListener (
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val favorite:Boolean=true
                    if(favorite){

                        val editor=getSharedPreferences(" com.example.myapplication.movie_detail.view.DetailActivity", Context.MODE_PRIVATE).edit()
                        editor.putBoolean("Favorite Added",true)
                        editor.apply()
                        saveFavorite()
                        Toast.makeText(this@DetailActivity,"Added to Favorite",Toast.LENGTH_SHORT).show()

                    }else{
                        val moviId=intent.extras.getInt("id")
                        favoriteDbHelper?.deleteFavorite(moviId)
                        val editor=getSharedPreferences(" com.example.myapplication.movie_detail.view.DetailActivity", Context.MODE_PRIVATE).edit()
                        editor.putBoolean("Favorite Removed",true)
                        editor.apply()
                        Toast.makeText(this@DetailActivity,"Removed from Favorite",Toast.LENGTH_SHORT).show()


                    }

                }

            })
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

    override fun setDataToViews(movie: MovieDetail) {

        movies= Movies()

        movies= Movies()
        movies?.setTitle(movie.getTitle().toString())
        movies?.setReleaseDate(movie.getReleaseDate().toString())
        movies?.setVoteAverage(movie.getVoteAverage())
        movies?.setPosterPath(movie.getPosterPath().toString())
        movies?.setOriginalLanguage(movie.getOriginalLanguage().toString())
        movies?.setOverview(movie.getOverview().toString())

        detail_title.text = movie.getTitle()
        releasedate.text = movie.getReleaseDate()
        vote_average.text = movie.getVoteAverage()?.toString()
        original_language.text = movie.getOriginalLanguage()
        detailoverview.text = movie.getOverview()
        loadTrailer(movie)
        if (!isFinishing) {
            Glide.with(this@DetailActivity)
                .load(BACK_DROP_URL + movie.getPosterPath())
                .placeholder(R.drawable.abc_ic_go_search_api_material)
                .into(details_image)

        }
    }


    override fun onResponseFailure(t: Throwable) {

        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
    }


   /* override fun getMovieDetail() {

        repository.getMovieDetail(movieID, object : OnGetMovieCallback {
            override fun onSuccess(movie: MovieDetail) {

                 movies= Movies()

                movies= Movies()
                movies?.setTitle(movie.getTitle().toString())
                movies?.setReleaseDate(movie.getReleaseDate().toString())
                movies?.setVoteAverage(movie.getVoteAverage())
                movies?.setPosterPath(movie.getPosterPath().toString())
                movies?.setOriginalLanguage(movie.getOriginalLanguage().toString())
                movies?.setOverview(movie.getOverview().toString())

                detail_title.text = movie.getTitle()
                releasedate.text = movie.getReleaseDate()
                vote_average.text = movie.getVoteAverage()?.toString()
                original_language.text = movie.getOriginalLanguage()
                detailoverview.text = movie.getOverview()
                loadTrailer(movie)
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

    }*/

     fun loadTrailer(movie: MovieDetail) {
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

                            showTrailer(YOUTUBE_VIDEO_URL+trailer.getKey())

                        }

                    })

                    Glide.with(this@DetailActivity)
                        .load(YOUTUBE_THUMBNAIL_URL + trailer.getKey())
                        .placeholder(R.drawable.youtube)
                        .centerCrop()
                        .into(thumbnail)

                    movieTrailers.addView(parent)

                }
            }

            override fun onError() {
                trailersLabel.visibility = View.GONE
            }

        })
    }

     fun showTrailer(url: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



     fun saveFavorite() {
        val favorites = Movies()
        val rate=movies?.getVoteAverage()
        val thumbnail=movies?.getPosterPath()
        val movieName=movies?.getTitle()
        val releasedate=movies?.getReleaseDate()

        favorites.setId(movieID)
        favorites.setOriginalTitle(movieName.toString())
        favorites.setPosterPath(thumbnail.toString())
        favorites.setReleaseDate(releasedate.toString())
        favorites.setVoteAverage(rate)
        //favorites?.setOverview(synopsis)
        favoriteDbHelper?.addFavorite(favorites)


    }

    /*private fun onFavoriteClick(){

        movies?.let { detailPresenter?.onFavoriteClick(it) }

    }*/

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






