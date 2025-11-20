package com.eco.musicplayer.audioplayer.music.activity.ads

import android.app.Activity
import android.os.Build
import com.eco.ads.EcoFullScreenCallback
import com.eco.ads.EcoInfoAdsCallback
import com.eco.ads.appopen.EcoAppOpenAd
import com.eco.ads.appopen.EcoAppOpenAdListener
import com.eco.musicplayer.audioplayer.music.activity.AdsApplication
import com.eco.musicplayer.audioplayer.music.activity.util.getSharedInt
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.concurrent.TimeUnit

class AppOpenSplashManager(private val activity: Activity) {
    var appOpenAd: AppOpenAd? = null
    var ecoAppOpen: EcoAppOpenAd? = null
    var isShowingAd: Boolean = false
    private var adsStatus = 0
    private val adsLoading = 1
    private val adsLoaded = 2
    private val adsLoadFailed = 3
    private var appOpenListener: OpenListener? = null
    private var timeLoadAds: Long = 0

    init {
        appOpenListener = activity as OpenListener
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    fun fetchAd(idAds: String) {
        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                loadAdCross()
            }

            override fun onAdLoaded(p0: AppOpenAd) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    appOpenAd!!.setImmersiveMode(true)
                }
                adsStatus = adsLoaded
                this@AppOpenSplashManager.appOpenAd = p0
            }
        }
        val request = getAdRequest()
        adsStatus = adsLoading
        timeLoadAds = System.currentTimeMillis()
        AppOpenAd.load(
            activity,
            idAds ?: "", request, loadCallback
        )
    }

    private fun loadAdCross() {
        ecoAppOpen = EcoAppOpenAd.Builder(activity).setAdId(ID_CROSS_INTER_MAIN)
            .setAppOpenAdListener(object : EcoAppOpenAdListener() {
                override fun onAdLoaded(ecoAppOpenAd: EcoAppOpenAd) {
                    adsStatus = adsLoaded
                }

                override fun onAdFailToLoad(error: String) {
                    ecoAppOpen = null
                    adsStatus = adsLoadFailed
                    appOpenListener?.onOpenAppLoadFailed()
                }
            }).setColorNameAppButtonSkip("#474747").setColorNameAppButtonSkip("#00FFC2")
            .setColorTextContinueToApp("#80FFFFFF").build()
        ecoAppOpen?.load(activity)
    }

    private fun checkTime(timeOld: Long, timeCurrent: Long): Boolean {
        return TimeUnit.MILLISECONDS.toSeconds(timeCurrent - timeOld) > activity.getSharedInt(
            time_min_show_ads, 35
        )
    }

    fun showAd(activity: Activity): Boolean {
        if (!isShowingAd && checkTime(
                (activity.application as AdsApplication).lastTimeForShowInterAds,
                System.currentTimeMillis()
            ) && !(activity.application as AdsApplication).isShowFullAds
        ) {
            if (appOpenAd != null) {
                val fullScreenContentCallback: FullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            if (appOpenListener != null) appOpenListener?.onCloseOpenApp()
                            this@AppOpenSplashManager.appOpenAd = null
                            isShowingAd = false
                            (activity.application as AdsApplication).isShowFullAds = false
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            super.onAdFailedToShowFullScreenContent(p0)
                            if (appOpenListener != null) appOpenListener?.onCloseOpenApp()
                            (activity.application as AdsApplication).isShowFullAds=false
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            isShowingAd =true
                            (activity.application as AdsApplication).isShowFullAds =true
                            (activity.application as AdsApplication).lastTimeForShowInterAds =
                                System.currentTimeMillis()

                        }
                    }
                appOpenAd?.fullScreenContentCallback =fullScreenContentCallback
                appOpenAd?.show(activity)
            } else if (ecoAppOpen != null){
                ecoAppOpen?.setFullScreenCallback(object :EcoFullScreenCallback(){
                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isShowingAd =true
                        (activity.application as AdsApplication).isShowFullAds =true
                        (activity.application as AdsApplication).lastTimeForShowInterAds =System.currentTimeMillis()
                    }

                    override fun onAdFailedToShowFullScreenContent(error: String) {
                        super.onAdFailedToShowFullScreenContent(error)
                        if (appOpenListener != null) appOpenListener?.onCloseOpenApp()
                        (activity.application as AdsApplication).isShowFullAds =false
                        if (ecoAppOpen != null){
                            ecoAppOpen?.setFullScreenCallback(null)
                            ecoAppOpen =null
                        }
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        if (appOpenListener != null) appOpenListener?.onCloseOpenApp()
                        this@AppOpenSplashManager.appOpenAd =null
                        isShowingAd =false
                        (activity.application as AdsApplication).isShowFullAds =false
                        if (ecoAppOpen != null){
                            ecoAppOpen?.setFullScreenCallback(null)
                            ecoAppOpen =null
                        }
                    }

                    override fun onFullscreenAdsResume() {
                        super.onFullscreenAdsResume()
                        if (appOpenListener != null){
                            appOpenListener?.onEcoFullscreenAdsResume()
                        }
                    }
                })
                ecoAppOpen?.setInfoAdsCallback(object :EcoInfoAdsCallback(){
                    override fun onRemoveAllAdsClicked() {
                        super.onRemoveAllAdsClicked()
                        if (appOpenListener != null){
                            appOpenListener?.onEcoCrossRemoveAllAdsClicked()
                        }
                    }
                })
                ecoAppOpen?.show(activity)
            }
            (activity.application as AdsApplication).lastTimeForShowInterAds =System.currentTimeMillis()
            return true
        }
        return false
    }

    fun closeCross(){
        ecoAppOpen?.close()
    }
    fun isLoadFailed():Boolean{
        return adsStatus == adsLoadFailed
    }
    fun isLoaded():Boolean{
        return adsStatus == adsLoaded
    }
    fun isLoading():Boolean{
        return adsStatus ==adsLoading
    }
}


interface   OpenListener {
    fun onCloseOpenApp()
    fun onOpenAppLoadFailed()
    fun onEcoFullscreenAdsResume()
    fun onEcoCrossRemoveAllAdsClicked()
}