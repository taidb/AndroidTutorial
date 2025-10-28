package com.eco.musicplayer.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eco.musicplayer.audioplayer.music.R

class ImageAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_FULL = 0
        private const val TYPE_HALF = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 3 == 0) TYPE_FULL else TYPE_HALF
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_FULL) {
            val view = inflater.inflate(R.layout.item_image_large, parent, false)
            FullViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.thumbnail, parent, false)
            HalfViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image = images[position]
        if (holder is FullViewHolder) holder.bind(image)
        if (holder is HalfViewHolder) holder.bind(image)
    }

    override fun getItemCount(): Int = images.size

    inner class FullViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.idImage)
        fun bind(resId: Int) {
            Glide.with(imageView.context).load(resId).into(imageView)
        }
    }

    inner class HalfViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.idImage)
        fun bind(resId: Int) {
            Glide.with(imageView.context).load(resId).into(imageView)
        }
    }
}
