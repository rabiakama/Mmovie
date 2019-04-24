package com.example.myapplication.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.Videos
import kotlinx.android.synthetic.main.activity_detail.view.*

import kotlinx.android.synthetic.main.movie_item.view.*


class TrailerAdapter(private var trailerList:List<Videos>): RecyclerView.Adapter<TrailerAdapter.ViewHolderV>() {




    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolderV {
        val view = LayoutInflater.from(group.context).inflate(R.layout.videos_row, group)
        return ViewHolderV(view)
    }

    override fun onBindViewHolder(holder: ViewHolderV, position: Int) {
        //p0.titletxt.setText(trailerList.get(p1).getName())
        holder.bindTo(trailerList[position])
    }


    override fun getItemCount(): Int {
        return trailerList.size
    }


    inner class ViewHolderV(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titletxt by lazy { itemView.findViewById<TextView>(R.id.details_video_title) }
        //val thumbnail by lazy { itemView.findViewById<TextView>(R.id.details_video_icon) }
        val videoUrl="https://www.youtube.com/watch?v="

        fun bindTo(vd: Videos) {

            titletxt.text = vd.getName()
            var pos=adapterPosition
            var videoId=trailerList.get(pos).getKey()

            Glide.with(itemView.context)
                .load(videoUrl+videoId)
                .placeholder(R.drawable.abc_ic_go_search_api_material)
                .into(itemView.details_image)

        }




    }
}