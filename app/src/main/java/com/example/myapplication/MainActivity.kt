package com.example.myapplication


import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import android.os.Parcelable
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.widget.SearchView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.animation.ScaleAnimation
import android.widget.AbsListView
import com.arlib.floatingsearchview.FloatingSearchView
import com.example.myapplication.model.Movies
import com.example.myapplication.model.MoviesResponse
import com.example.myapplication.repository.Repository
import com.example.myapplication.service.Api
import com.example.myapplication.service.Client
import java.lang.Exception





class MainActivity : AppCompatActivity(),ComponentCallbacks2 {

    var currentItem:Int?=null
    var totalItem:Int?=null
    var scrollItem:Int?=null
    var loading:Boolean=false
    private val POPULAR_TASK = 0
    private val UPCOMING_TASK = 1
    private val TOP_RATED_TASK = 2
    //private val NOW_PLAYING_TASK = 3
    //private val MOVIES = 4
    private var moviesAdapter: MoviesAdapter?=null
    private var arraylistmovies: ArrayList<Movies> = arrayListOf()
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository(Client.getClient()!!.create(Api::class.java))
        if (savedInstanceState != null) {
            displayData()
        } else {
            initViews()
        }

        recylerView_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    loading = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                currentItem = linearLayoutManager.childCount
                totalItem = linearLayoutManager.itemCount
                scrollItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()

                if (!loading && (currentItem!! + scrollItem!! == totalItem)) {
                    loading = false
                    loadJson()
                }
            }


        })
        loadJson()

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.popular_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(POPULAR_TASK)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.top_rated_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(TOP_RATED_TASK)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.upcoming_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(UPCOMING_TASK)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.my_favorite_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    //fetchFavMovies()
                    return@setOnNavigationItemSelectedListener true
                }


                else -> fetchMovies(POPULAR_TASK)
            }
            false
        }
    }


    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_view).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                moviesAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                moviesAdapter?.filter?.filter(newText)
                return true

            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        return if (id == R.id.search_view) {
            true
        } else
            return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (search_view.isSearchBarFocused) {
            search_view.clearQuery()
       }else
        super.onBackPressed()
    }

    private fun onItemClicked() {
        Toast.makeText(this@MainActivity,"you clicked",Toast.LENGTH_LONG).show()

    }*/
    private fun displayData() {

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recylerView_main.layoutManager = GridLayoutManager(this, 2)
        } else {
            recylerView_main.layoutManager = GridLayoutManager(this, 4)
        }
        recylerView_main.itemAnimator = DefaultItemAnimator()
        restoreLayoutManagerPosition()
        recylerView_main.adapter=moviesAdapter
        moviesAdapter?.notifyDataSetChanged()

    }

    private fun restoreLayoutManagerPosition() {
        val savedRecyclerLayoutState: Parcelable? = null
        if (savedRecyclerLayoutState != null) {
            recylerView_main.layoutManager?.onRestoreInstanceState(savedRecyclerLayoutState)
        }
    }

    private fun initViews() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recylerView_main.layoutManager = GridLayoutManager(this, 2)

        } else {
            recylerView_main.layoutManager = GridLayoutManager(this, 4)
        }
        recylerView_main.itemAnimator = DefaultItemAnimator()
        loadJson()
    }


    private fun loadJson() {

        try {
            repository.getMovies().enqueue(object:Callback<MoviesResponse>{
                override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        arraylistmovies.addAll(response.body()!!.results!!)
                        recylerView_main.layoutManager =
                            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                       moviesAdapter=MoviesAdapter(arraylistmovies)
                        recylerView_main.adapter = moviesAdapter
                        recylerView_main.smoothScrollToPosition(0)
                    }

                }

                override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    recylerView_main.visibility = View.INVISIBLE
                    recylerView_main.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchMovies(taskId: Int): Boolean {
        recylerView_main.visibility = View.INVISIBLE
        recylerView_main.visibility = View.VISIBLE
        when (taskId) {
            POPULAR_TASK -> {

                repository.getPopularMovies().enqueue(object:Callback<MoviesResponse>{
                    override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            arraylistmovies.addAll(response.body()!!.results!!)
                            recylerView_main.layoutManager =
                                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            moviesAdapter=MoviesAdapter(arraylistmovies)
                            recylerView_main.adapter = moviesAdapter
                            recylerView_main.smoothScrollToPosition(0)
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
                return true
            }
            TOP_RATED_TASK -> {

                repository.getTopMovies()
                return true
            }
            UPCOMING_TASK -> {
                repository.getComingMovies()
                return true
            }
            else -> return true
        }
    }


    }


















