package com.eco.musicplayer.audioplayer.music.activity.ads.inter

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eco.ads.EcoFullScreenCallback
import com.eco.ads.EcoInfoAdsCallback
import com.eco.ads.interstitial.EcoInterstitialAd
import com.eco.ads.interstitial.EcoInterstitialAdListener
import com.eco.musicplayer.audioplayer.music.activity.CompressVideoApplication
import com.eco.musicplayer.audioplayer.music.activity.ads.PREFS_PURCHASED
import com.eco.musicplayer.audioplayer.music.activity.ads.time_min_show_ads
import com.eco.musicplayer.audioplayer.music.activity.getApplication
import com.eco.musicplayer.audioplayer.music.activity.util.getSharedInt
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialUtil {
    private var admobInterstitial: InterstitialAd? = null
    private var ecoInterstitialAd: EcoInterstitialAd? = null
    private var abIdAdmob = "ca-app-pub-3940256099942544/1033173712"
    private var crossId = ""
    var listener: InterstitialListener? = null
    private var state = STATE_ADS_READY_TO_LOAD
    fun setAdsId(adIdAdmob: String, crossId: String) {
        this.abIdAdmob = adIdAdmob
        this.crossId = crossId
    }

    fun isLoaded() = state == STATE_ADS_LOADED
    fun isLoading() = state == STATE_ADS_LOADING
    fun isError() = state == STATE_ADS_LOAD_ERROR
    private fun isLoadedAdmob(): Boolean {
        return admobInterstitial != null
    }

    private fun isLoadedCross(): Boolean {
        return ecoInterstitialAd?.isLoaded() ?: false
    }

    fun closeEcoInterstitial() {
        ecoInterstitialAd?.close()
    }

    fun showAd(activity: AppCompatActivity){
        if (state == STATE_ADS_SHOWING) return

        activity.getApplication {
            val interCoolOffTime = it.getSharedInt(time_min_show_ads, 35)
            val currentTime = System.currentTimeMillis()

            if (currentTime - it.lastTimeForShowInterAds > interCoolOffTime) {
                // Ưu tiên Admob trước
                admobInterstitial?.let { interstitialAd ->
                    checkShowAd(activity, interstitialAd)
                } ?: run {
                    // Fallback sang Eco
                    ecoInterstitialAd?.let { ecoInterstitialAd ->
                        if (ecoInterstitialAd.isLoaded()) {
                            ecoInterstitialAd.show(activity)
                        } else {
                            listener?.onAdFailToShow("EcoInterstitialAd not loaded")
                            loadAds(activity) // Tự động reload nếu cả 2 đều lỗi
                        }
                    } ?: run {
                        listener?.onAdFailToShow("No ads available")
                        loadAds(activity) // Tự động reload
                    }
                }
            } else {
                listener?.onAdFailToShow("Cool off time limited by $interCoolOffTime")
            }
        }
    }

    fun isInCoolOffTime(application: CompressVideoApplication) :Boolean{
        val interCoolOffTime =application.getSharedInt(time_min_show_ads,35)
        val currentTime =System.currentTimeMillis()
        return currentTime -application.lastTimeForShowInterAds <=interCoolOffTime
    }

    fun clear(){
        clearAdmob()
        clearCross()
        state = STATE_ADS_READY_TO_LOAD
        listener=null
    }

    private fun clearAdmob(){
        admobInterstitial?.fullScreenContentCallback =null
        admobInterstitial =null
    }

    private fun clearCross(){
        ecoInterstitialAd =null
    }

    private fun checkShowAd(activity: AppCompatActivity ,admobInterstitial: InterstitialAd ){
        admobInterstitial.fullScreenContentCallback =object  :FullScreenContentCallback(){
            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                activity.getApplication {
                    it.isShowFullAds =true
                }
                state = STATE_ADS_SHOWING
                listener?.onAdShowedFullScreen()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                activity.getApplication{
                    it.isShowFullAds=false
                }
                state = STATE_ADS_READY_TO_LOAD
                listener?.onAdFailToShow(p0.message)
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                activity.getApplication {
                    it.isShowFullAds =false
                    it.lastTimeForShowInterAds =System.currentTimeMillis()
                }
                state = STATE_ADS_READY_TO_LOAD
                listener?.onAdDismissedFullScreen()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                activity.getApplication {
                    it.isShowFullAds=true
                }
            }

        }
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.VANILLA_ICE_CREAM){
            admobInterstitial.setImmersiveMode(true)
        }
        admobInterstitial.show(activity)

    }


    fun loadAds(context: Context) {
        if (state == STATE_ADS_READY_TO_LOAD) {
            state = STATE_ADS_LOADING
            loadAdmob(context, abIdAdmob) { adAdmob ->
                this.admobInterstitial = adAdmob
                if (adAdmob != null) {
                    state = STATE_ADS_LOADED
                    Log.d("Interstitial", "Admob loaded successfully")
                } else {
                    Log.d("Interstitial", "Admob failed, loading Eco")
                }
                // LUÔN load Eco làm fallback
                loadCross(context)
            }
        }
    }

    private fun checkLoadCross(admobInterstitial: InterstitialAd?, context: Context) {
        if (admobInterstitial == null) {
            loadCross(context) // Chỉ load Eco khi Admob null
        } else {
            state = STATE_ADS_LOADED
            this.admobInterstitial = admobInterstitial
        }
    }

    private fun loadAdmob(
        context: Context,
        id: String,
        callback: ((InterstitialAd?) -> Unit)? = null
    ) {
        InterstitialAd.load(
            context,
            id,
            AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    callback?.let { it(p0) }
                    Log.e("TAG", "onAdLoaded: ")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    callback?.let { it(null) }
                    Log.e("TAG", "onAdFailedToLoad: ")
                }
            }
        )
    }

    private fun loadCross(context: Context) {
        ecoInterstitialAd = EcoInterstitialAd.Builder(context).setAdId(crossId).build().apply {
            setInterstitialAdListener(object : EcoInterstitialAdListener() {
                override fun onAdLoaded(ecoInterstitialAd: EcoInterstitialAd) {
                    super.onAdLoaded(ecoInterstitialAd)
                    // Chỉ set state = LOADED nếu Admob cũng failed
                    if (admobInterstitial == null) {
                        state = STATE_ADS_LOADED
                    }
                    Log.d("Interstitial", "Eco interstitial loaded")
                }

                override fun onAdFailToLoad(error: String) {
                    super.onAdFailToLoad(error)
                    // Chỉ set state = ERROR nếu cả Admob và Eco đều failed
                    if (admobInterstitial == null) {
                        state = STATE_ADS_LOAD_ERROR
                    }
                    context.getApplication { it.isShowFullAds = false }
                    Log.e("Interstitial", "Eco load failed: $error")
                }
            })

            setInfoAdCallback(object  : EcoInfoAdsCallback() {
                override fun onRemoveAllAdsClicked() {
                    listener?.onEcoRemoveAllAdsClicked()
                }
        })
            setFullScreenCallback(object : EcoFullScreenCallback(){
                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    context.getApplication{it.isShowFullAds =false}
                    state= STATE_ADS_READY_TO_LOAD
                    listener?.onAdFailToShow(error)
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    context.getApplication{
                        it.isShowFullAds =false
                        it.lastTimeForShowInterAds =System.currentTimeMillis()
                    }
                    state = STATE_ADS_READY_TO_LOAD
                    listener?.onAdDismissedFullScreen()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    context.getApplication{it.isShowFullAds=true}
                    state = STATE_ADS_SHOWING
                    listener?.onAdShowedFullScreen()
                }

                override fun onFullscreenAdsResume() {
                    super.onFullscreenAdsResume()
                    listener?.onEcoFullScreenAdsResume()
                }

            })
            load(context)

        }
    }



}