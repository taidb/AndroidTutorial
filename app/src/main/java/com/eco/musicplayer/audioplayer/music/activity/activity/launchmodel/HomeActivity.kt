package com.eco.musicplayer.audioplayer.music.activity.activity.launchmodel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    //chỉ có 1 instance duy nhất trong task,clear các activity phía trên
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newQuery = intent.getStringExtra("query")
        if (newQuery != null) {
            println()
            // Xử lý truy vấn mới ở đây
        }else{
            //
        }
    }
}