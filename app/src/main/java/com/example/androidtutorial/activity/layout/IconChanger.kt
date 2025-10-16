package com.example.androidtutorial.activity.layout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtutorial.R
import com.example.androidtutorial.activity.adapter.ImageAdapter
import com.example.androidtutorial.activity.adater.OptionalAdapter
import com.example.androidtutorial.databinding.ActivityIconChangerBinding

class IconChanger : AppCompatActivity() {
    private lateinit var binding: ActivityIconChangerBinding

    private val options = arrayOf("Image", "D.I.Widget", "Calendar", "Date", "Time", "Clock")
    private val optionAdapter = OptionalAdapter(options)

    private val imageList = listOf(
        R.drawable.search,
        R.drawable.search,
        R.drawable.search,
        R.drawable.search,
        R.drawable.search,
        R.drawable.search
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIconChangerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupImageRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rcvClick.apply {
            layoutManager = LinearLayoutManager(this@IconChanger, LinearLayoutManager.HORIZONTAL, false)
            adapter = optionAdapter
        }
    }

    private fun setupImageRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Ảnh full chiếm 2 cột
                return if (position % 3 == 0) 2 else 1
            }
        }

        binding.rcvImage.apply {
            layoutManager = gridLayoutManager
            adapter = ImageAdapter(imageList)
        }
    }
}
