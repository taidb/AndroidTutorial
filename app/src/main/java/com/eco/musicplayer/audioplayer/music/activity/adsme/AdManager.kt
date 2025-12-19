package com.eco.musicplayer.audioplayer.music.activity.adsme

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView

import androidx.annotation.RequiresPermission
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd

import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
import com.google.android.gms.ads.nativead.NativeAdView


class AdManager(private val application: Application) {
    private var appOpenAd: AppOpenAd? = null
    private var isShowingAd = false
    private var loadCallback: (() -> Unit)? = null

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var nativeAd: NativeAd? = null

    private var lastOpenAdShownTime: Long = 0
    private var lastInterstitialAdShownTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private var loadingOverlay: View? = null

    init {
        MobileAds.initialize(application) { initializationStatus ->
            Log.d("AdManager", "AdMob initialized")
        }
    }

    fun loadAppOpenAd(activity: Activity, onAdLoaded: (() -> Unit)? = null) {
        Log.d("AdManager", "=== LOADING APP OPEN AD ===")
        Log.d("AdManager", "Activity: ${activity.javaClass.simpleName}")

        this.loadCallback = onAdLoaded

        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            activity,
            AdConfig.ADMOB_OPEN_AD_ID,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d("AdManager", "APP OPEN AD: onAdLoaded() CALLED!")
                    appOpenAd = ad
                    loadCallback?.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("AdManager", "APP OPEN AD: Failed to load - ${loadAdError.message}")
                    Log.e("AdManager", "Error code: ${loadAdError.code}")
                    appOpenAd = null
                    // Vẫn gọi callback để tiếp tục
                    loadCallback?.invoke()
                }
            }
        )
    }

    fun showAppOpenAd(activity: Activity): Boolean {
        Log.d("AdManager", "=== SHOW APP OPEN AD ===")
        Log.d("AdManager", "isShowingAd: $isShowingAd, appOpenAd != null: ${appOpenAd != null}")

        if (isShowingAd) {
            Log.d("AdManager", "Cannot show: Another ad is showing")
            return false
        }

        if (appOpenAd == null) {
            Log.d("AdManager", "Cannot show: No appOpenAd available")
            return false
        }

        Log.d("AdManager", "Attempting to show App Open Ad...")


        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("AdManager", "AppOpenAd: Dismissed")
                appOpenAd = null
                isShowingAd = false
                // Gọi callback để chuyển sang màn hình chính
                if (activity is SplashActivity) {
                    activity.proceedToMainActivity()
                }
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e("AdManager", "AppOpenAd: Failed to show - ${adError.message}")
                appOpenAd = null
                isShowingAd = false
                if (activity is SplashActivity) {
                    activity.proceedToMainActivity()
                }
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdManager", "AppOpenAd: Showed successfully")
                isShowingAd = true
            }
        }

        try {
            appOpenAd?.show(activity)
            return true
        } catch (e: Exception) {
            Log.e("AdManager", "AppOpenAd: Exception while showing - ${e.message}")
            return false
        }
    }
    fun loadInterstitialAd(activity: Activity) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInterstitialAdShownTime < AdConfig.INTERSTITIAL_AD_INTERVAL) {
            Log.d("AdManager", "Interstitial: Skipped due to cool-off period")
            return
        }

        val timeoutTask = Runnable {
            Log.d("AdManager", "Interstitial: Load timeout")
            interstitialAd = null
        }
        handler.postDelayed(timeoutTask, AdConfig.AD_LOAD_TIMEOUT)

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            AdConfig.ADMOB_INTERSTITIAL_AD_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    handler.removeCallbacks(timeoutTask)
                    interstitialAd = ad
                    Log.d("AdManager", "Interstitial: Loaded successfully")

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            isShowingAd = false
                            lastInterstitialAdShownTime = System.currentTimeMillis()
                            Log.d("AdManager", "Interstitial: Dismissed")
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            interstitialAd = null
                            isShowingAd = false
                            Log.e("AdManager", "Interstitial: Failed to show - ${adError.message}")
                        }

                        override fun onAdShowedFullScreenContent() {
                            isShowingAd = true
                            Log.d("AdManager", "Interstitial: Showed successfully")
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    handler.removeCallbacks(timeoutTask)
                    interstitialAd = null
                    Log.e("AdManager", "Interstitial: Failed to load - ${loadAdError.message}")
                }
            }
        )
    }

    fun showInterstitialAd(activity: Activity, onAdDismissed: (() -> Unit)? = null): Boolean {
        if (isShowingAd) {
            Log.d("AdManager", "Interstitial: Another ad is showing")
            return false
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInterstitialAdShownTime < AdConfig.INTERSTITIAL_AD_INTERVAL) {
            Log.d("AdManager", "Interstitial: Cool-off period not passed")
            return false
        }

        interstitialAd?.let { ad ->
            showLoadingOverlay(activity)

            handler.postDelayed({
                ad.show(activity)
                hideLoadingOverlay()
            }, 300)

            val originalCallback = ad.fullScreenContentCallback
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    originalCallback?.onAdDismissedFullScreenContent()
                    onAdDismissed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    originalCallback?.onAdFailedToShowFullScreenContent(adError)
                    onAdDismissed?.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    originalCallback?.onAdShowedFullScreenContent()
                }
            }

            return true
        }

        Log.d("AdManager", "Interstitial: No ad available")
        onAdDismissed?.invoke()
        return false
    }

    fun loadRewardedAd(activity: Activity) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            activity,
            AdConfig.ADMOB_REWARDED_AD_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("AdManager", "RewardedAd: Loaded successfully")

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            rewardedAd = null
                            isShowingAd = false
                            Log.d("AdManager", "RewardedAd: Dismissed")
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            rewardedAd = null
                            isShowingAd = false
                            Log.e("AdManager", "RewardedAd: Failed to show - ${adError.message}")
                        }

                        override fun onAdShowedFullScreenContent() {
                            isShowingAd = true
                            Log.d("AdManager", "RewardedAd: Showed successfully")
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                    Log.e("AdManager", "RewardedAd: Failed to load - ${loadAdError.message}")
                }
            }
        )
    }

    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: (() -> Unit)? = null,
        onAdDismissed: (() -> Unit)? = null
    ): Boolean {
        if (isShowingAd) {
            Log.d("AdManager", "RewardedAd: Another ad is showing")
            return false
        }

        rewardedAd?.let { ad ->
            showLoadingOverlay(activity)

            handler.postDelayed({
                ad.show(activity) { rewardItem ->
                    Log.d("AdManager", "RewardedAd: Reward earned - ${rewardItem.amount} ${rewardItem.type}")
                    onRewardEarned?.invoke()
                }
                hideLoadingOverlay()
            }, 300)

            return true
        }

        Log.d("AdManager", "RewardedAd: No ad available")
        return false
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    fun setupBannerAd(activity: Activity, adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("AdManager", "BannerAd: Loaded successfully")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.e("AdManager", "BannerAd: Failed to load - ${loadAdError.message}")
            }
        }
    }

    private fun showLoadingOverlay(activity: Activity) {
        if (loadingOverlay == null) {
            loadingOverlay = View(activity).apply {
                setBackgroundColor(0x88000000.toInt())
                isClickable = true
                isFocusable = true
            }

            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
            rootView.addView(loadingOverlay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun hideLoadingOverlay() {
        loadingOverlay?.let {
            val parent = it.parent as? ViewGroup
            parent?.removeView(it)
            loadingOverlay = null
        }
    }

    fun destroyNativeAd() {
        nativeAd?.destroy()
        nativeAd = null
    }

    fun loadNativeAd(
        context: Context,
        adType: String,
        callback: (NativeAd?) -> Unit
    ) {
        Log.d("AdManager", "Loading native ad with type: $adType")

        // Chọn đúng Ad Unit ID cho video
        val adUnitId = when (adType) {
            "TYPE_3" -> AdConfig.ADMOB_NATIVE_VIDEO // Ad Unit đặc biệt cho video
            else -> AdConfig.ADMOB_NATIVE_AD_ID
        }

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val nativeAdOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setMediaAspectRatio(MediaAspectRatio.LANDSCAPE)
            .build()

        val adLoader = AdLoader.Builder(context, adUnitId) // Sử dụng đúng ID
            .forNativeAd { nativeAd ->
                Log.d("AdManager", "Native ad loaded successfully")
                callback(nativeAd)
            }
            .withNativeAdOptions(nativeAdOptions)
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView,
        adType: String
    ) {
        // Populate basic views cho tất cả các loại
        populateBasicViews(nativeAd, adView)

        // Tuỳ chỉnh hiển thị theo loại quảng cáo
        when (adType) {
            "TYPE_1" -> populateType1NativeAd(nativeAd, adView)
            "TYPE_3" -> populateType3NativeAd(nativeAd, adView)
            "TYPE_4" -> populateType4NativeAd(nativeAd, adView)
        }

        // Đăng ký NativeAd với view
        adView.setNativeAd(nativeAd)
    }

    private fun populateBasicViews(nativeAd: NativeAd, adView: NativeAdView) {
        // Headline
        (adView.headlineView as? TextView)?.text = nativeAd.headline

        // Body (nếu có)
        (adView.bodyView as? TextView)?.text = nativeAd.body

        // Call to Action (nếu có)
        (adView.callToActionView as? Button)?.text = nativeAd.callToAction

        // Advertiser (nếu có)
        (adView.advertiserView as? TextView)?.text = nativeAd.advertiser

        // Icon
        if (nativeAd.icon != null) {
            (adView.iconView as? ImageView)?.setImageDrawable(nativeAd.icon?.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } else {
            adView.iconView?.visibility = View.GONE
        }
    }


    private fun populateType1NativeAd(nativeAd: NativeAd, adView: NativeAdView) {
        // Populate Type 1 (Large with Image)
        (adView.headlineView as? TextView)?.text = nativeAd.headline
        (adView.bodyView as? TextView)?.text = nativeAd.body
        (adView.callToActionView as? Button)?.text = nativeAd.callToAction
        (adView.advertiserView as? TextView)?.text = nativeAd.advertiser

        // Icon
        if (nativeAd.icon != null) {
            (adView.iconView as? ImageView)?.setImageDrawable(nativeAd.icon?.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } else {
            adView.iconView?.visibility = View.GONE
        }

        // Media content
        if (nativeAd.mediaContent != null) {
            (adView.mediaView as? MediaView)?.setMediaContent(nativeAd.mediaContent!!)
            adView.mediaView?.visibility = View.VISIBLE
        }

        // Star rating
        if (nativeAd.starRating != null) {
            (adView.starRatingView as? RatingBar)?.rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        } else {
            adView.starRatingView?.visibility = View.GONE
        }

        adView.setNativeAd(nativeAd)
    }


    private fun populateType3NativeAd(nativeAd: NativeAd, adView: NativeAdView) {
        val mediaContent = nativeAd.mediaContent

        if (mediaContent != null && mediaContent.hasVideoContent()) {
            // Chỉ thiết lập media content
            adView.mediaView?.setMediaContent(mediaContent)
            adView.mediaView?.visibility = View.VISIBLE

            // Chỉ log video events
            mediaContent.videoController.videoLifecycleCallbacks =
                object : VideoController.VideoLifecycleCallbacks() {
                    override fun onVideoStart() {
                        Log.d("NativeVideo", "Video started")
                    }

                    override fun onVideoEnd() {
                        Log.d("NativeVideo", "Video ended")
                    }
                }
        } else {
            adView.mediaView?.visibility = View.GONE
        }

        // Ẩn star rating nếu TYPE_3 không cần
        adView.starRatingView?.visibility = View.GONE
    }

    private fun populateType4NativeAd(nativeAd: NativeAd, adView: NativeAdView) {
        // Populate Type 4 (Carousel - simplified)
        (adView.headlineView as? TextView)?.text = nativeAd.headline
        (adView.callToActionView as? Button)?.text = nativeAd.callToAction

        // Icon
        if (nativeAd.icon != null) {
            (adView.iconView as? ImageView)?.setImageDrawable(nativeAd.icon?.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } else {
            adView.iconView?.visibility = View.GONE
        }

        adView.setNativeAd(nativeAd)
    }
}