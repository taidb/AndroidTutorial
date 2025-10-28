package com.eco.musicplayer.audioplayer.music.activity.adater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eco.musicplayer.audioplayer.music.databinding.ItemOptionalBinding

class OptionalAdapter(private val optionalList: Array<String>) :
    RecyclerView.Adapter<OptionalAdapter.optionalViewHolder>() {
    inner class optionalViewHolder(val binding: ItemOptionalBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): optionalViewHolder {
        return optionalViewHolder(
            ItemOptionalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return optionalList.size
    }

    override fun onBindViewHolder(holder: optionalViewHolder, position: Int) {
        val optional = optionalList[position]
        holder.binding.tvTitle.text = optional
        Log.d("OptionalAdapter", "Binding item: $optional")
    }
}