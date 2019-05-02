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
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.Movies
import com.example.myapplication.model.Videos
import kotlinx.android.synthetic.main.videos_row.view.*


class TrailerAdapter(private  var trailerList:ArrayList<Videos>): RecyclerView.Adapter<TrailerAdapter.ViewHolderV>() {

    private val itemClickListener:OnItemClickListener?=null

    // private lateinit var trailerList:List<Videos>
   lateinit var mContext: Context


    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolderV {
        val view = LayoutInflater.from(group.context).inflate(R.layout.videos_row, group,false)
        return ViewHolderV(view)

    }

    override fun onBindViewHolder(holder: ViewHolderV, position: Int) {
        //p0.titletxt.setText(trailerList.get(p1).getName())
        val trailer = trailerList[position]
        if (itemClickListener != null) {
            holder.bindTo(trailer, itemClickListener)
        }

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
       // val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.thumbnailVideo) }
        //val videoUrl="https://www.youtube.com/watch?v="
        val posterBasePath = "https://image.tmdb.org/t/p/w500/"

        fun bindTo(vd: Videos, listener: OnItemClickListener) {

            title.text = vd.getName()

            Glide.with(itemView.context)
                .load(posterBasePath + vd.getKey())
                .placeholder(R.drawable.abc_ic_go_search_api_material)
                .into(itemView.thumbnailVideo)

            itemView.setOnClickListener {object:View.OnClickListener{
                override fun onClick(v: View) {
                    //listener.onItemClicked(trailerList[adapterPosition])
                    val pos=adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        val clickedDataItem=trailerList.get(pos)
                        val videoId=trailerList.get(pos).getKey()
                        val intent=Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" +videoId))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("VIDEO_ID",videoId)
                        mContext.startActivity(intent)

                        Toast.makeText(v.context,"You clicked"+clickedDataItem.getName(),Toast.LENGTH_SHORT).show()
                }


                    }


                }


                //clickListener.onItemClicked(trailerList[adapterPosition])
            }
            //val videoId = trailerList.get(adapterPosition).getKey()


        }


    }
    interface OnItemClickListener {
        fun onItemClicked(trailer: Videos)
    }
}
