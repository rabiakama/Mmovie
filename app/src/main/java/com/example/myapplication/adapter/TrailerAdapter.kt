package com.example.myapplication.adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.Videos
import kotlinx.android.synthetic.main.videos_row.view.*




class TrailerAdapter(private  var trailerList:ArrayList<Videos>,val itemClickListener:OnItemClickListener): RecyclerView.Adapter<TrailerAdapter.ViewHolderV>() {


    // private lateinit var trailerList:List<Videos>
    var mContext: Context? = null


    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolderV {
        val view = LayoutInflater.from(group.context).inflate(R.layout.videos_row, group)
        return ViewHolderV(view)

    }

    override fun onBindViewHolder(holder: ViewHolderV, position: Int) {
        //p0.titletxt.setText(trailerList.get(p1).getName())
        val trailer = trailerList[position]
        holder.bindTo(trailer, itemClickListener)

    }


    override fun getItemCount(): Int {
        return trailerList.size
    }

    fun setMovies(video: ArrayList<Videos>) {
        // movielist = movie

        this.trailerList.addAll(video)
        notifyDataSetChanged()
    }



    inner class ViewHolderV(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val title by lazy { itemView.findViewById<TextView>(R.id.titleVideo)}
            val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.thumbnailVideo) }
            //val videoUrl="https://www.youtube.com/watch?v="

            val posterBasePath = "https://image.tmdb.org/t/p/w500/"

            fun bindTo(vd: Videos, listener: OnItemClickListener) {

                title.text = vd.getName()

                itemView.setOnClickListener {
                    listener.onItemClicked(trailerList[adapterPosition])
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val videoId = trailerList.get(adapterPosition).getKey()
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("VIDEO_ID", videoId)
                        mContext?.startActivity(intent)
                    }


                    //clickListener.onItemClicked(trailerList[adapterPosition])
                }
                //val videoId = trailerList.get(adapterPosition).getKey()
                Glide.with(itemView.context)
                    .load(posterBasePath + vd.getKey())
                    .placeholder(R.drawable.abc_ic_go_search_api_material)
                    .into(itemView.thumbnailVideo)

            }


    }
    interface OnItemClickListener {
        fun onItemClicked(trailer: Videos)
    }
}

