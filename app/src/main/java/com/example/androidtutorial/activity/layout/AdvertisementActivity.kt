package com.example.androidtutorial.activity.layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.example.androidtutorial.R


class AdvertisementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advertisement)

        val btnTrial = findViewById<AppCompatButton>(R.id.btnTrial)

        val btnClose = findViewById<AppCompatImageButton>(R.id.btnClose)

        val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.slide)
        btnTrial.startAnimation(shakeAnim)

        btnClose.setOnClickListener {
            Log.d("TEST", "Button clicked")
            val intent = Intent(this, S2NotesActivity::class.java)
            startActivity(intent)
        }
    }
}
