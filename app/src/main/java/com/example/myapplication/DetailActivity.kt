package com.example.myapplication



import android.content.*
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import com.bumptech.glide.Glide
import com.example.myapplication.repository.OnGetMovieCallback
import com.example.myapplication.repository.Repository
import com.example.myapplication.service.Api
import com.example.myapplication.service.Client
import kotlinx.android.synthetic.main.activity_detail.*
import android.support.v7.widget.LinearLayoutManager
import com.example.myapplication.model.Movies
import android.view.View
import android.widget.ImageView

import android.widget.Toast
import com.example.myapplication.adapter.TrailerAdapter
import com.example.myapplication.model.MovieDetail
import com.example.myapplication.model.Videos
import com.example.myapplication.repository.FavHelper
import com.example.myapplication.repository.OnGetTrailersCallback



@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity() {


    private val BACK_DROP_URL = "https://image.tmdb.org/t/p/w500/"
    private val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var saveMovieRecordNumber: Int? = null
    private val SAVE_MOVIE_SUCCESS = 10
    private val SAVE_MOVIE_FAIL = 11
    private  var movies:Movies?=null
    //private var favorite:Movies?=null
    private var mtrailerList: ArrayList<Videos>?= arrayListOf()
    lateinit var mtrailerAdapter: TrailerAdapter
    private val IS_FAVORITE = "isFavorite"
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
        // supportActionBar?.title=movie.getTitle()
        loadDetail()
        //loadTrailer()
        initCollapsingToolbar()



        /* favorite_button.setOnClickListener {
            val intent=Intent(this,FavoriteActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Added to Your Favorite Page",Toast.LENGTH_LONG).show()
        }*/


        favorite_button.setOnClickListener (
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val favorite:Boolean=true
                    if(favorite){

                        val editor=getSharedPreferences(" com.example.myapplication.DetailActivity", Context.MODE_PRIVATE).edit()
                        editor.putBoolean("Favorite Added",true)
                        editor.apply()
                        saveFavorite()
                        Toast.makeText(this@DetailActivity,"Added to Favorite",Toast.LENGTH_SHORT).show()

                    }else{
                        val moviId=intent.extras.getInt("id")
                        favoriteDbHelper?.deleteFavorite(moviId)
                        val editor=getSharedPreferences(" com.example.myapplication.DetailActivity", Context.MODE_PRIVATE).edit()
                        editor.putBoolean("Favorite Removed",true)
                        editor.apply()
                        Toast.makeText(this@DetailActivity,"Removed from Favorite",Toast.LENGTH_SHORT).show()


                    }

                }
                /* override fun onClick(v: View) {
                     val favorite: Boolean? = null
                     if (favorite!!) {
                         addMovieToFavorite()
                     }

                 }*/

            })
            //initView()
        }



    private fun initView() {
        val adapter: TrailerAdapter? = null
        val mLayoutManager = LinearLayoutManager(applicationContext)
        detailsRv.layoutManager = mLayoutManager
        detailsRv.adapter = adapter
        adapter?.notifyDataSetChanged()
        loadDetail()

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

                 movies=Movies()

                movies=Movies()
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

    }

    private fun loadTrailer(movie:MovieDetail) {
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

    private fun showTrailer(url: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

        /*val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieID))
            startActivity(intent)*/
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


    private fun checkIsMovieAlreadyInFav(movieId:String): Boolean {
        val database=favoriteDbHelper?.readableDatabase
        val selectString="SELECT *FROM" +FavHelper.TABLE_NAME + "WHERE"
         FavHelper.COLUMN_MOVIEID + "=?"

        val cursor=database?.rawQuery(selectString, arrayOf(movieId))
        //String[] {movieıd} yerine arrayOf
        val count=cursor?.count
        cursor?.close()
        database?.close()
        return count!! >0
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




