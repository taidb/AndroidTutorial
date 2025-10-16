package com.example.androidtutorial.activity.layout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtutorial.activity.adater.OptionalAdapter
import com.example.androidtutorial.databinding.ActivityIconChangerBinding

class IconChanger : AppCompatActivity() {
    private lateinit var binding: ActivityIconChangerBinding
    private val options = arrayOf("Image", "D.I.Widget", "Calendar", "Date", "Time", "Clock")
    private val optionAdapter = OptionalAdapter(options)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIconChangerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rcvClick.apply {
            layoutManager = LinearLayoutManager(this@IconChanger, LinearLayoutManager.HORIZONTAL, false)
            adapter = optionAdapter
        }
    }
}