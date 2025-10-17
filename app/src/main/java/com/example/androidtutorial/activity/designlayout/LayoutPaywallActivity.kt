package com.example.androidtutorial.activity.designlayout

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityLayoutPaywallBinding

class LayoutPaywallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutPaywallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_paywall)
        binding=ActivityLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}