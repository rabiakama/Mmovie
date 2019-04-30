package com.example.myapplication



import android.content.ActivityNotFoundException
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
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerViewAccessibilityDelegate
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView

import android.widget.Toast
import com.example.myapplication.adapter.TrailerAdapter
import com.example.myapplication.repository.FavHelper
import com.example.myapplication.repository.OnGetTrailersCallback
import kotlinx.android.synthetic.main.videos_row.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity() {

    private val BACK_DROP_URL = "https://image.tmdb.org/t/p/w500/"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
     private var movieID: Int = 0
    var MOVIE_ID = "movie_id"

    private val favoriteDbHelper: FavHelper? = null
    private val favorite: Movies? = null


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


        favorite_button.setOnClickListener { object:View.OnClickListener{
            override fun onClick(v: View) {
                val  favorite:Boolean=false

                if(favorite){
                    val editor=getSharedPreferences("com.example.myapplication.DetailActivity", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("Favorite Added",true)
                    editor.apply()//neden commit yerine kullanılıyor
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        saveFavorite()
                    }
                    Toast.makeText(this@DetailActivity,"Added to your favorite",Toast.LENGTH_SHORT).show()

                }
                else {
                    val movie_id = intent.extras.getInt("id")
                    favoriteDbHelper?.deleteFavorite(movie_id)
                    val editor =
                        getSharedPreferences("com.example.myapplication.DetailActivity", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("Favorite Removed", true)
                    editor.apply()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        saveFavorite()
                    }
                    Toast.makeText(this@DetailActivity, "Removed to your favorite", Toast.LENGTH_SHORT).show()
                }


        }
    }
            initView()


}
        }



    private fun initView() {
        val movieList:ArrayList<Movies>
        val adapter:MoviesAdapter?=null
        val mLayoutManager=LinearLayoutManager(applicationContext)
        detailsRv.layoutManager=mLayoutManager
        detailsRv.adapter=adapter
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


    private fun loadDetail(){

        repository.getMovieDetail(movieID ,object: OnGetMovieCallback{
            override fun onSuccess(movie: MovieDetail) {

                detail_title.setText(movie.getTitle())
                releasedate.setText(movie.getReleaseDate())
                vote_average.setText(movie.getVoteAverage()!!.toDouble().toString())
                original_language.setText(movie.getOriginalLanguage())
                detailoverview.setText(movie.getOverview())
                loadTrailer()
                if(!isFinishing){
                    Glide.with(this@DetailActivity)
                        .load(BACK_DROP_URL+ movie.getPosterPath())
                        .placeholder(R.drawable.abc_ic_go_search_api_material)
                        .into(details_image)

                }
            }

            override fun onError() {
             finish()
            }

        })

    }


    private fun loadTrailer(){
        repository.getTrailers(movieID,object :OnGetTrailersCallback{
            override fun onSuccess(trailers: List<Videos>?) {
                trailersLabel.visibility=View.VISIBLE
                movieTrailers.removeAllViews()
                for ( trailer :Videos in trailers!!) {
                    val parent:View=layoutInflater.inflate(R.layout.videos_row,movieTrailers,false)
                    val thumbnail:ImageView=parent.findViewById(R.id.thumnailVideo)
                    thumbnail.requestLayout()

                    thumbnail.setOnClickListener(object:View.OnClickListener{
                        override fun onClick(v: View?) {
                            showTrailer(YOUTUBE_VIDEO_URL,trailer.getKey())

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
                trailersLabel.visibility=View.GONE
            }

        })
    }

    private fun showTrailer(url: String, key: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }catch (ex:ActivityNotFoundException ){
            val intent=Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+movieID))
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.P)
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




    }



