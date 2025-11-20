package com.eco.musicplayer.audioplayer.music.activity.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.AdsApplication
import com.eco.musicplayer.audioplayer.music.activity.ads.BANNER_MAIN
import com.eco.musicplayer.audioplayer.music.activity.ads.BannerAdUtil
import com.eco.musicplayer.audioplayer.music.activity.ads.GG_MAIN_INTERSTITIAL
import com.eco.musicplayer.audioplayer.music.activity.ads.ID_CROSS_BANNER_MAIN
import com.eco.musicplayer.audioplayer.music.activity.ads.ID_CROSS_INTER_MAIN
import com.eco.musicplayer.audioplayer.music.activity.ads.inter.InterstitialListener
import com.eco.musicplayer.audioplayer.music.activity.ads.inter.InterstitialUtil
import com.eco.musicplayer.audioplayer.music.activity.network.isNetworkAvailable
import com.eco.musicplayer.audioplayer.music.databinding.ActivityAdsBinding
import kotlinx.coroutines.Job

class AdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdsBinding

    val mainInterstitiaUtil by lazy { InterstitialUtil() }
    private var jobLoadAds: Job? = null
    val permissionUtil by lazy { isNetworkAvailable() }
    private val bannerAdUtil: BannerAdUtil by lazy {
        BannerAdUtil(this, object : BannerAdUtil.BannerAdsAdmobListener {
            override fun onBannerAdmobAdsLoaded() {
                // Xử lý khi banner tải xong
            }

            override fun onBannerAdmobAdsLoadFailed() {
                // Xử lý khi banner tải thất bại
            }

            override fun onBannerAdmobAdsClicked() {
                // Xử lý khi banner được click
            }

            override fun onCrossRemoveAdClicked() {
                // Xử lý khi click remove ads
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ẩn tất cả banner quảng cáo ban đầu
        binding.layoutContainAllAds.visibility = View.GONE
        binding.layoutContainAllAdsTop.visibility = View.GONE
        binding.layoutAdsCross.visibility = View.GONE
        binding.layoutAdsCrossTop.visibility = View.GONE

        // Hiển thị toàn bộ màn hình loading - SỬ DỤNG TRỰC TIẾP TỪ BINDING
        // Tìm các view trong included layout
        val layoutLoading = binding.root.findViewById<View>(R.id.layoutLoading)
        val txtContainAds = binding.root.findViewById<View>(R.id.txtContainAds)

        // Kiểm tra và hiển thị loading
        if (layoutLoading != null) {
            layoutLoading.visibility = View.VISIBLE
        }
        if (txtContainAds != null) {
            txtContainAds.visibility = View.VISIBLE
        }

        loadBannerAds()
        showInterAds()

        if (!(application as AdsApplication).isShowAdsWhenOpen) {
            mainInterstitiaUtil.setAdsId(
                GG_MAIN_INTERSTITIAL,
                ID_CROSS_INTER_MAIN
            )
            if (mainInterstitiaUtil.isLoaded().not() &&
                mainInterstitiaUtil.isLoading().not() &&
                mainInterstitiaUtil.isInCoolOffTime(application as AdsApplication).not()
            ) {
                mainInterstitiaUtil.loadAds(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Không load banner ads trên onResume nữa
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadBannerAds() {
        binding.layoutContainAllAds.visibility =View.VISIBLE
            binding.layoutContainAllAds.post {
                if (true) {
                    binding.layoutContainAllAds.visibility =View.VISIBLE
                    binding.layoutContainAllAdsTop.visibility =View.GONE
                    bannerAdUtil.loadInlineAdaptiveAds(
                        BANNER_MAIN,
                        ID_CROSS_BANNER_MAIN,
                        binding.layoutAds,
                        binding.layoutContainAllAds.height,
                        binding.layoutAdsCross
                    )
                    binding.layoutAds.visibility =View.VISIBLE
                } else {
                    val height = binding.layoutContainAllAds.height
                    val layoutParams = binding.layoutContainAllAdsTop.layoutParams
                    layoutParams.height = height
                    binding.layoutContainAllAdsTop.layoutParams = layoutParams

                    binding.layoutContainAllAds.visibility =View.GONE
                    binding.layoutContainAllAdsTop.visibility =View.VISIBLE
                    binding.layoutContainAllAdsTop.post {
                        bannerAdUtil.loadInlineAdaptiveAds(
                            BANNER_MAIN,
                            ID_CROSS_BANNER_MAIN,
                            binding.layoutAdsTop,
                            height,
                            binding.layoutAdsCrossTop
                        )

                        }
                    }
                    binding.layoutAdsTop.visibility =View.VISIBLE
                }

    }

    private fun showInterAds() {
        jobLoadAds?.cancel()
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoadingAndShowBanners()
        }, 3000)
        showMainInter(object : InterstitialListener() {
            override fun onAdDismissedFullScreen() {
                super.onAdDismissedFullScreen()
                // Khi tắt quảng cáo fullscreen, ẩn loading và hiển thị banner
                hideLoadingAndShowBanners()
                mainInterstitiaUtil.clear()
            }

            override fun onAdFailToShow(error: String) {
                super.onAdFailToShow(error)
                // Nếu không hiển thị được quảng cáo, vẫn ẩn loading và hiển thị banner
                hideLoadingAndShowBanners()
            }
        })

        (application as AdsApplication).isShowAdsWhenOpen = true
    }

    private fun hideLoadingAndShowBanners() {
        // Tìm lại các view để tránh lỗi
        val layoutLoading = binding.root.findViewById<View>(R.id.layoutLoading)
        val txtContainAds = binding.root.findViewById<View>(R.id.txtContainAds)

        // Ẩn toàn bộ màn hình loading
        if (layoutLoading != null) {
            layoutLoading.visibility = View.GONE
        }
        if (txtContainAds != null) {
            txtContainAds.visibility = View.GONE
        }

        // Hiển thị banner quảng cáo
        binding.layoutContainAllAds.visibility = View.VISIBLE
        binding.layoutContainAllAdsTop.visibility = View.VISIBLE
        binding.layoutAdsCross.visibility = View.VISIBLE
        binding.layoutAdsCrossTop.visibility = View.VISIBLE
    }

    fun showMainInter(listener: InterstitialListener? = null) {
        if (mainInterstitiaUtil.isLoaded()) {
            mainInterstitiaUtil.listener = listener
            mainInterstitiaUtil.showAd(this)
        } else {
            listener?.onAdFailToShow("Main inter not loaded")
            // Nếu interstitial chưa load được, vẫn hiển thị banner
            hideLoadingAndShowBanners()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainInterstitiaUtil.clear()
    }
}