package com.example.androidtutorial.activity.designlayout

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityLayoutPaywall3Binding

class LayoutPaywallActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutPaywall3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLayoutPaywall3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.idYearly.setOnClickListener {
            selectTextView(binding.idYearly, binding.idWeekly)

        }
        binding.idWeekly.setOnClickListener {
            selectTextView(binding.idWeekly, binding.idYearly)
        }
        binding.root.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, statusBarHeight, 0, 0)
            insets
        }


    }

    private fun selectTextView(selected: AppCompatTextView, unselected: AppCompatTextView) {
        selected.setBackgroundResource(R.drawable.rounded_corners_6)
        selected.setTextColor(ContextCompat.getColor(this, R.color.dark))
        unselected.background = null
        unselected.setTextColor(ContextCompat.getColor(this, R.color.white_EAF5F8))
        selected.animate().scaleX(1.05f).scaleY(1.05f).setDuration(120).start()
        unselected.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
    }

}