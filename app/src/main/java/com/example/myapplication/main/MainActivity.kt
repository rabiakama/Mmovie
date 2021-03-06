package com.example.myapplication.main


import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.sqlite.SQLiteDatabase
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
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.support.v7.widget.SearchView
import android.widget.LinearLayout
import com.example.myapplication.MoviesAdapter
import com.example.myapplication.R
import com.example.myapplication.movie.Movies
import com.example.myapplication.movie.MoviesResponse
import com.example.myapplication.data.local.FavHelper
import com.example.myapplication.repository.Repository
import com.example.myapplication.data.remote.model.Api
import com.example.myapplication.data.remote.retrofit.Client
import com.example.myapplication.movie_detail.view.DetailActivity
import java.lang.Exception

class MainActivity : AppCompatActivity(),ComponentCallbacks2,
    MoviesAdapter.OnItemClickListener,MainActivityContract.View {


    private lateinit var mainPresenter: MainPresenter
    var currentItem: Int? = null
    var totalItem: Int? = null
    var scrollItem: Int? = null
    var loading: Boolean = false
    val shownInOneScreen=20
    lateinit var items: ArrayList<String>

    private val MOVIES = 0
    private val POPULAR_TASK = 1
    private val TOP_RATED_TASK = 3
    private val NOW_PLAYING_TASK = 2
    private var moviesAdapter: MoviesAdapter? = null
    private var arraylistmovies: ArrayList<Movies> = arrayListOf()
    private lateinit var repository: Repository

    private var favoriteDbHelper: FavHelper? = null
    private lateinit var searchView: SearchView
    private val activity = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository(Client.getClient()!!.create(Api::class.java))

        this.mainPresenter= MainPresenter()
        this.mainPresenter.setView(this)


        var mLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recylerView_main.layoutManager = mLayoutManager

        if (savedInstanceState != null) {
            displayData()
        } else {
            initViews()
        }

        items=ArrayList<String>()
        for (i in 1..shownInOneScreen){
            items.add("Item: $i")
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
                        loadMovies()
                    }


            }


        })

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.all_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(MOVIES)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.popular_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(POPULAR_TASK)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.playing_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(NOW_PLAYING_TASK)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.top_rated_movies -> {

                    recylerView_main.smoothScrollToPosition(0)
                    fetchMovies(TOP_RATED_TASK)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.my_favorite_movies -> {
                    recylerView_main.smoothScrollToPosition(0)
                    initViews2()
                    return@setOnNavigationItemSelectedListener true
                }


                else -> return@setOnNavigationItemSelectedListener false
            }

        }
    }



    private fun initViews2() {
        val factory: SQLiteDatabase.CursorFactory? = null
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recylerView_main.layoutManager = GridLayoutManager(this, 2)
        } else {
            recylerView_main.layoutManager = GridLayoutManager(this, 4)
        }
        recylerView_main.itemAnimator = DefaultItemAnimator()
        recylerView_main.adapter = moviesAdapter
        moviesAdapter?.notifyDataSetChanged()
        favoriteDbHelper = FavHelper(this.activity, "favorite.db", factory, 2)

        getAllFavorite()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
         searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //moviesAdapter?.filter?.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    moviesAdapter?.filter?.filter(newText)
                    moviesAdapter?.notifyDataSetChanged()
                    return true

                }
            })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_search) {
            true
        } else
            return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified=true
       }else
        super.onBackPressed()
    }


    private fun displayData() {

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recylerView_main.layoutManager = GridLayoutManager(this, 2)
        } else {
            recylerView_main.layoutManager = GridLayoutManager(this, 4)
        }
        recylerView_main.itemAnimator = DefaultItemAnimator()
        restoreLayoutManagerPosition()
        recylerView_main.adapter = moviesAdapter
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
        loadMovies()
    }


     fun fetchMovies(taskId: Int): Boolean {
        recylerView_main.visibility = View.INVISIBLE
        recylerView_main.visibility = View.VISIBLE
        when (taskId) {
            MOVIES -> {
                repository.getMovies().enqueue(object : Callback<MoviesResponse> {
                    override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            arraylistmovies.clear()
                            arraylistmovies.addAll(response.body()!!.results!!)
                            recylerView_main.layoutManager =
                                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            moviesAdapter = MoviesAdapter(arraylistmovies, this@MainActivity)
                            recylerView_main.adapter = moviesAdapter
                            recylerView_main.smoothScrollToPosition(0)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                        showError("Error Movies")
                    }
                })
                return true
            }

            POPULAR_TASK -> {

                repository.getPopularMovies().enqueue(object : Callback<MoviesResponse> {
                    override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            arraylistmovies.clear()
                            arraylistmovies.addAll(response.body()!!.results!!)
                            recylerView_main.layoutManager =
                                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            moviesAdapter = MoviesAdapter(arraylistmovies, this@MainActivity)
                            recylerView_main.adapter = moviesAdapter
                            recylerView_main.smoothScrollToPosition(0)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                        showError("Error Popular")
                    }
                })
                return true
            }
            NOW_PLAYING_TASK -> {

                repository.getNowPlayingMovies().enqueue(object : Callback<MoviesResponse> {
                    override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            arraylistmovies.clear()
                            arraylistmovies.addAll(response.body()!!.results!!)
                            recylerView_main.layoutManager =
                                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            moviesAdapter = MoviesAdapter(arraylistmovies, this@MainActivity)
                            recylerView_main.adapter = moviesAdapter
                            recylerView_main.smoothScrollToPosition(0)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                        showError("Error NowPlaying")
                    }
                })
                return true
            }
            TOP_RATED_TASK -> {
                repository.getTopMovies().enqueue(object : Callback<MoviesResponse> {
                    override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            arraylistmovies.clear()
                            arraylistmovies.addAll(response.body()!!.results!!)
                            recylerView_main.layoutManager =
                                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            moviesAdapter = MoviesAdapter(arraylistmovies, this@MainActivity)
                            recylerView_main.adapter = moviesAdapter
                            recylerView_main.smoothScrollToPosition(0)
                            //getDetails()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                        showError("Error TopRated")
                    }
                })
                return true
            }



            else -> return true
        }
    }

    override fun onItemClicked(movie: Movies) {
        showError("Detail Page")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIE_ID", movie.getId())
        //intent.getStringExtra("MOVIE_ID")
        this.startActivity(intent)
    }

     fun loadMovies() {

        try {
            repository.getMovies().enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(call: Call<MoviesResponse>, response: retrofit2.Response<MoviesResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        arraylistmovies.addAll(response.body()!!.results!!)
                        recylerView_main.layoutManager =
                            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        moviesAdapter = MoviesAdapter(arraylistmovies, this@MainActivity)
                        recylerView_main.adapter = moviesAdapter
                        recylerView_main.smoothScrollToPosition(0)
                    }

                }

                override fun onFailure(call: retrofit2.Call<MoviesResponse>, t: Throwable) {
                    showError("Error")
                    recylerView_main.visibility = View.INVISIBLE
                    recylerView_main.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            showError("Error")
        }
    }


     fun getAllFavorite() {
        class FavoritesTask : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                arraylistmovies.clear()
                    arraylistmovies.addAll(favoriteDbHelper!!.getAllFavorite())

                    return null
            }

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                moviesAdapter?.notifyDataSetChanged()
            }
        }
        FavoritesTask().execute()
    }

    override fun showError(value: String) {
        Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()

    }





}





















