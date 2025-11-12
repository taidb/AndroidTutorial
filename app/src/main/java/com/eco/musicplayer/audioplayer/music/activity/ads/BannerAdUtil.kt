package com.eco.musicplayer.audioplayer.music.activity.ads

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eco.ads.EcoInfoAdsCallback
import com.eco.ads.banner.EcoBannerAdListener
import com.eco.ads.banner.EcoBannerAdView

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

open class BannerAdUtil(var activity: AppCompatActivity, var listener: BannerAdsAdmobListener) {

    private var listenerLifeCycleCallback: LifecycleEventObserver? = null

    private var adStatus = 0
    private var adLoaded = 1
    private var adLoadError = 2

    private var adView: AdView? = null
    private var ecoBannerAdView: EcoBannerAdView? = null

    init {
        listenerLifeCycleCallback = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    adView?.resume()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    adView?.pause()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    adView?.destroy()
                    adView = null
                    ecoBannerAdView?.destroy()
                    ecoBannerAdView = null
                    listenerLifeCycleCallback?.let {
                        activity.lifecycle.removeObserver(it)
                    }
                }

                else -> {

                }
            }
        }.apply { activity.lifecycle.addObserver(this) }
    }

    fun loadInlineAdaptiveAds(
        adsId: String?,
        crossAdId: String,
        layoutAds: LinearLayoutCompat,
        maxheight: Int,
        containerCross: LinearLayoutCompat?
    ) {
        adView?.destroy()
        adView = null
        adView = AdView(activity)
        adView?.adUnitId = adsId ?: ""
        val adRequest = AdRequest.Builder().build()
        val adSize: AdSize = getAdSizeInline(maxheight)
        adView?.setAdSize(adSize)
        adView?.loadAd(adRequest)
        adView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                layoutAds.visibility = View.VISIBLE
                containerCross?.visibility = View.GONE
                layoutAds.removeAllViews()
                if (adView?.parent != null) {
                    (adView?.parent as? ViewGroup)?.removeView(adView)
                }
                layoutAds.addView(adView)
                listener.onBannerAdmobAdsLoaded()
                adStatus = adLoaded
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                layoutAds.visibility = View.GONE
                if (containerCross != null) {
                    containerCross.visibility = View.VISIBLE
                    loadCrossAd(crossAdId, containerCross)
                }
            }

            override fun onAdClicked() {
                listener.onBannerAdmobAdsClicked()
                super.onAdClicked()
            }
        }
    }

    fun loadInlineAdaptiveAds(
        adsId: String?,
        crossAdId: String,
        layoutAds: LinearLayoutCompat,
        maxheight: Int,
        isEnableCollapse: Boolean = false,
        isIgnoreCross: Boolean = false
    ) {
        adView?.destroy()
        adView = null
        adView = AdView(activity)
        adView?.adUnitId = adsId ?: ""

        val adRequest = if (isEnableCollapse) {
            val extras = Bundle()
            extras.putString("collapsible", "bottom")
            AdRequest.Builder()
                .addNetworkExtrasBundle(com.google.ads.mediation.admob.AdMobAdapter::class.java, extras)
                .build()
        } else {
            AdRequest.Builder().build()
        }

        val adSize: AdSize = if (isEnableCollapse) {
            getAdSizeInlineCollapse(maxheight)
        } else {
            getAdSizeInline(maxheight)
        }
        adView?.setAdSize(adSize)

        adView?.loadAd(adRequest)
        adView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                layoutAds.removeAllViews()
                if (adView?.parent != null) {
                    (adView?.parent as? ViewGroup)?.removeView(adView)
                }
                layoutAds.addView(adView)
                listener.onBannerAdmobAdsLoaded()
                adStatus = adLoaded
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                if (isIgnoreCross) {
                    adStatus = adLoadError
                    listener.onBannerAdmobAdsLoadFailed()
                } else {
                    loadCrossAd(crossAdId, layoutAds)
                }
            }

            override fun onAdClicked() {
                listener.onBannerAdmobAdsClicked()
                super.onAdClicked()
            }
        }
    }

    private fun loadCrossAd(idCross: String, layoutAds: LinearLayoutCompat) {
        layoutAds.removeAllViews()
        if (ecoBannerAdView == null) {
            ecoBannerAdView = EcoBannerAdView.Builder(activity)
                .setAdId(idCross)
                .setBannerAdsListener(object : EcoBannerAdListener() {
                    override fun onAdFailedToLoad(error: String) {
                        adStatus = adLoadError
                        listener.onBannerAdmobAdsLoadFailed()
                    }

                    override fun onAdLoaded() {
                        listener.onBannerAdmobAdsLoaded()
                        adStatus = adLoaded
                    }
                }).build()

            ecoBannerAdView?.setInfoAdsCallback(object : EcoInfoAdsCallback() {
                override fun onRemoveAllAdsClicked() {
                    listener.onCrossRemoveAdClicked()
                }
            })
        }
        ecoBannerAdView?.load(layoutAds)
    }

    fun isLoaded(): Boolean {
        return adStatus == adLoaded
    }

    fun isLoadError(): Boolean {
        return adStatus == adLoadError
    }

    fun isLoading(): Boolean {
        return adStatus == 0
    }

    private fun getAdSize(): AdSize {
        //Determine the screen width to use for the ad width.
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    private fun getAdSizeInline(maxHeight: Int): AdSize {
        //Determine the screen width to use for the ad width.
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val widthPixels = outMetrics.widthPixels * 95f / 100f
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()
        val adMaxHeight = (maxHeight / density).toInt()

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getInlineAdaptiveBannerAdSize(adWidth, adMaxHeight)
    }

    private fun getAdSizeInlineCollapse(maxHeight: Int): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val widthPixels = outMetrics.widthPixels
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()
        val adMaxHeight = (maxHeight / density).toInt()
        return AdSize.getInlineAdaptiveBannerAdSize(adWidth, adMaxHeight)
    }

    interface BannerAdsAdmobListener {
        fun onBannerAdmobAdsLoaded()
        fun onBannerAdmobAdsLoadFailed()
        fun onBannerAdmobAdsClicked()
        fun onCrossRemoveAdClicked()
    }
}