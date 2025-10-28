package com.eco.musicplayer.audioplayer.music.activity.activity.launchmodel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.activity.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        //hoạt động trong các task riêng biệt, không share task với các activity khác
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}