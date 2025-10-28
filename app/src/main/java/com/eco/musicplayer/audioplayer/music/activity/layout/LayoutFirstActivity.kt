package com.eco.musicplayer.audioplayer.music.activity.layout

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutFirstBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LayoutFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_layout_first)
        binding=ActivityLayoutFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startProgreessAndNavigate()
    }
    private fun startProgreessAndNavigate(){
        lifecycleScope.launch {
            for (i in 0..100){
                binding.seekBarProgress.progress=i
                delay(30)
            }
            startActivity(Intent(this@LayoutFirstActivity,AdvertisementActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left)
            finish()
        }
    }
}