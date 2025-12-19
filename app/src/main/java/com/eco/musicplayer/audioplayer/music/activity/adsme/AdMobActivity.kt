package com.eco.musicplayer.audioplayer.music.activity.adsme

import android.Manifest
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.activity.activity.AdDemoApp
import com.eco.musicplayer.audioplayer.music.databinding.ActivityAdMobBinding

class AdMobActivity : AppCompatActivity() {
    private lateinit var adManager: AdManager
    private lateinit var binding: ActivityAdMobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdMobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adManager = (application as AdDemoApp).adManager
        setupClickListeners()
        loadInitialAds()
    }

    private fun setupClickListeners() {
        binding.apply {
            btnShowInterstitial.setOnClickListener {
                val intent = Intent(this@AdMobActivity, InterstitialActivity::class.java)
                startActivity(intent)
            }

            btnShowRewarded.setOnClickListener {
                val intent = Intent(this@AdMobActivity, RewardedActivity::class.java)
                startActivity(intent)
            }

            // Các loại Native Ad
            btnNativeType1.setOnClickListener {
                val intent = Intent(this@AdMobActivity, NativeAdActivity::class.java).apply {
                    putExtra("AD_TYPE", "TYPE_1")
                }
                startActivity(intent)
            }


            btnNativeType3.setOnClickListener {
                val intent = Intent(this@AdMobActivity, NativeAdActivity::class.java).apply {
                    putExtra("AD_TYPE", "TYPE_3")
                }
                startActivity(intent)
            }

            btnNativeType4.setOnClickListener {
                val intent = Intent(this@AdMobActivity, NativeAdActivity::class.java).apply {
                    putExtra("AD_TYPE", "TYPE_4")
                }
                startActivity(intent)
            }
        }
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    private fun loadInitialAds() {
        try {
            // Load banner ad
            adManager.setupBannerAd(this, binding.adViewBanner)

            // Load native ad mẫu

            // Chuẩn bị các quảng cáo
            adManager.loadInterstitialAd(this)
            adManager.loadRewardedAd(this)

        } catch (e: SecurityException) {
            Toast.makeText(this, "Lỗi: Không có quyền Internet", Toast.LENGTH_SHORT).show()
        }
    }




    override fun onResume() {
        super.onResume()
        binding.adViewBanner?.resume()
    }

    override fun onPause() {
        binding.adViewBanner?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.adViewBanner?.destroy()
        adManager.destroyNativeAd()
        super.onDestroy()
    }
}