package com.example.myapplication


import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.squareup.picasso.Picasso
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.model.Movies
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE = "movie_details"
    }

    private lateinit var title: TextView
    private lateinit var backdropImage: ImageView
    private lateinit var overview: TextView
    private lateinit var rating: TextView
    private lateinit var releaseDate: TextView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = intent
        val movie = intent.getParcelableExtra<Movies>(EXTRA_MOVIE)

        println("Detail movie YYYYYYYYY" + movie)

        backdropImage = details_back_drop
        releaseDate = releasedate
        overview = detailoverview
        rating = vote_average

        Picasso.get().load("https://image.tmdb.org/t/p/w500/${movie.getBackdropPath()}")
            .into(backdropImage)
        title.text=movie.getTitle()
        releaseDate.text = movie.getReleaseDate()
        rating.text = movie.getVoteAverage().toString()
        overview.text = movie.getOverview()



    }



}


