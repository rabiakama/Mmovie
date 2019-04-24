package com.example.myapplication


import android.content.Context
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myapplication.model.Movies
import kotlinx.android.synthetic.main.movie_item.view.*
import android.widget.AdapterView.OnItemClickListener




class MoviesAdapter(var movielist:ArrayList<Movies>,val itemClickListener: OnItemClickListener):RecyclerView.Adapter<MoviesAdapter.ViewHolder>(),Filterable {
    private var movlist: MutableList<Movies>? = null
    private lateinit var context:Context


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    movielist = movlist as ArrayList<Movies>
                } else {
                    val filteredList = ArrayList<Movies>()
                    for (row in movlist!!) {
                        if (row.getTitle()!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    movielist = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = movielist
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, filterResult: FilterResults) {
                movielist = filterResult.values as ArrayList<Movies>
                notifyDataSetChanged()
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val movie=movielist[position]

        holder.bindTo(movie,itemClickListener)


    }



    fun setMovies(movie: ArrayList<Movies>) {
       // movielist = movie
        this.movielist.addAll(movie)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return movielist.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val titletxt by lazy { itemView.findViewById<TextView>(R.id.titleTextView) }
        val voteaverage by lazy { itemView.findViewById<TextView>(R.id.voteaverageitem) }
        val releasetxt by lazy { itemView.findViewById<TextView>(R.id.releaseDateTextView) }
        val posterBasePath = "https://image.tmdb.org/t/p/w500/"

        fun bindTo(
            mv: Movies,
            clickListener: OnItemClickListener
        ) {

            titletxt.text = mv.getTitle()
            releasetxt.text = mv.getReleaseDate()
            voteaverage.text = mv.getVoteAverage().toString()
            itemView.setOnClickListener {
                clickListener.onItemClicked(mv)
            }

            Glide.with(itemView.context)
                .load(posterBasePath + mv.getPosterPath())
                .placeholder(R.drawable.abc_ic_go_search_api_material)
                //.thumbnail(Glide.with(itemView.context).load(R.drawable.abc_ic_go_search_api_material))
                .into(itemView.posterImageView)

        }
    }
    interface OnItemClickListener{
        fun onItemClicked(movie: Movies)
    }


}








