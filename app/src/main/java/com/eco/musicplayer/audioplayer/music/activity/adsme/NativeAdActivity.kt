package com.eco.musicplayer.audioplayer.music.activity.adsme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.activity.AdDemoApp
import com.eco.musicplayer.audioplayer.music.databinding.ActivityNativeAdBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class NativeAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNativeAdBinding
    private lateinit var adManager: AdManager
    private lateinit var adType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adManager = (application as AdDemoApp).adManager

        // Lấy loại quảng cáo từ Intent
        adType = intent.getStringExtra("AD_TYPE") ?: "TYPE_1"

        setupUI()
        loadNativeAd()
    }

    private fun setupUI() {
        when (adType) {
            "TYPE_1" -> {
                binding.tvTitle.text = "Native Ad Lớn có Hình Ảnh"
                binding.containerAd.removeAllViews()
                binding.containerAd.addView(createType1NativeAdLayout())
            }

            "TYPE_3" -> {
                binding.tvTitle.text = "Native Ad Video"
                binding.containerAd.removeAllViews()
                binding.containerAd.addView(createType3NativeAdLayout())
            }
            "TYPE_4" -> {
                binding.tvTitle.text = "Native Ad nhỏ"
                binding.containerAd.removeAllViews()
                binding.containerAd.addView(createType4NativeAdLayout())
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnReloadAd.setOnClickListener {
            loadNativeAd()
        }
    }

    private fun loadNativeAd() {
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.containerAd.visibility = android.view.View.GONE

        adManager.loadNativeAd(this, adType) { nativeAd ->
            runOnUiThread {
                binding.progressBar.visibility = android.view.View.GONE
                binding.containerAd.visibility = android.view.View.VISIBLE

                if (nativeAd != null) {
                    displayNativeAd(nativeAd)
                    Toast.makeText(this, "Quảng cáo đã tải thành công!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Không thể tải quảng cáo. Vui lòng thử lại!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun displayNativeAd(nativeAd: NativeAd) {
        val adView = binding.containerAd.getChildAt(0) as? NativeAdView
        if (adView != null) {
            // Populate thông tin cơ bản
            adManager.populateNativeAdView(nativeAd, adView, adType)

            // Kiểm tra nếu có video
            if (adType == "TYPE_3" && nativeAd.mediaContent != null) {
                Log.d("NativeAdActivity", "Native ad has video content")

                // Kiểm tra thông tin video
                val mediaContent = nativeAd.mediaContent
                if (mediaContent != null) {
                    if (mediaContent.hasVideoContent()) {
                        Log.d("NativeAdActivity", "Video duration: ${mediaContent.duration}")
                        Log.d("NativeAdActivity", "Aspect ratio: ${mediaContent.aspectRatio}")

                        // Hiển thị MediaView
                        adView.mediaView?.visibility = View.VISIBLE
                    } else {
                        Log.d("NativeAdActivity", "No video content in media")
                    }
                }
            }
        }
    }

    private fun createType1NativeAdLayout(): NativeAdView {
        val inflater = LayoutInflater.from(this)
        val adView = inflater.inflate(R.layout.native_ad_type_1, null) as NativeAdView

        // Thiết lập layout cho Type 1
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)

        return adView
    }

    private fun createType3NativeAdLayout(): NativeAdView {
        val inflater = LayoutInflater.from(this)
        val adView = inflater.inflate(R.layout.native_ad_type_3, null) as NativeAdView

        // Thiết lập layout cho Type 3 (video)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        return adView
    }

    private fun createType4NativeAdLayout(): NativeAdView {
        val inflater = LayoutInflater.from(this)
        val adView = inflater.inflate(R.layout.native_ad_type_4, null) as NativeAdView

        // Thiết lập layout cho Type 4 (carousel)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)

        return adView
    }

    override fun onDestroy() {
        adManager.destroyNativeAd()
        super.onDestroy()
    }
}