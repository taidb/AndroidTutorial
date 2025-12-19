package com.eco.musicplayer.audioplayer.music.activity.adsme

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.activity.AdDemoApp
import com.eco.musicplayer.audioplayer.music.databinding.ActivityInterstitialBinding

class InterstitialActivity : AppCompatActivity() {
    private lateinit var adManager: AdManager
    private lateinit var binding: ActivityInterstitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adManager = (application as AdDemoApp).adManager

        binding.apply {
            tvTitle.text = "Interstitial Ad Demo"

            btnShowInterstitial.setOnClickListener {
                updateStatus("Đang chuẩn bị quảng cáo...")

                if (!adManager.showInterstitialAd(this@InterstitialActivity, onAdDismissed = {
                        updateStatus("Đã xem xong quảng cáo")
                        Toast.makeText(this@InterstitialActivity, "Quảng cáo đã kết thúc", Toast.LENGTH_SHORT).show()
                    })) {
                    adManager.loadInterstitialAd(this@InterstitialActivity)
                    updateStatus("Quảng cáo chưa sẵn sàng, đang tải...")
                    Toast.makeText(this@InterstitialActivity, "Vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
                }
            }

            comeBack.setOnClickListener {
                finish()
            }

            updateStatus("Sẵn sàng xem Interstitial Ad")
        }
    }

    private fun updateStatus(message: String) {
        binding.tvStatus.text = "Trạng thái: $message"
    }
}