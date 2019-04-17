package com.example.myapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.model.Videos
import kotlinx.android.synthetic.main.videos_row.view.*

class VideosAdapter (private val videolist: List<Videos>, private val callback: (Videos) -> (Unit)) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    override fun onBindViewHolder(holder: VideosAdapter.VideoViewHolder, position: Int) {
        holder.bind(videolist[position], callback)
    }

    override fun getItemCount(): Int {
        return videolist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosAdapter.VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.videos_row, parent, false)
        return VideoViewHolder(view)
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(video: Videos, callback: (Videos) -> Unit) {
            with(itemView) {
                details_video_title.text = video.type
                //video.name
                setOnClickListener { callback(video) }
            }
        }
    }
}