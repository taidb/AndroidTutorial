package com.example.androidtutorial.activity.layout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.androidtutorial.R


class AdvertisementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advertisement)

        val btnTrial = findViewById<AppCompatButton>(R.id.btnTrial)
        val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        btnTrial.startAnimation(shakeAnim)
    }
}
