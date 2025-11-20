package com.eco.musicplayer.audioplayer.music.activity.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.eco.musicplayer.audioplayer.music.activity.AdsApplication
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.Date
import java.util.concurrent.TimeUnit

class AppOpenManager(private val myApplication: AdsApplication, idAds:String) : Application.ActivityLifecycleCallbacks, LifecycleObserver {
    var appOpenAd: AppOpenAd? =
        null // Quảng cáo khi mở ứng dụng được dùng để hiển thị quảng cáo khi người dùng mở ứng dụng của bạn
    private lateinit var currentActivity: Activity
    var appOpenListener: AppOpenListener? = null
    var isShowingAd = false
    var loadTime = 0
    private var idAds = ""
    private var adReloadCount: Int = 0
    private var isLeftScreen = false
    private var adsStatus = 0
    private val adsLoading = 1
    private val adsLoaded = 2
    private val adsLoadFailed = 3

    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    private fun isIAPBought(): Boolean {
        return currentActivity != null && currentActivity!!.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        ).getBoolean(PREFS_PURCHASED, false)
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    private fun wasLoadTimeLessThanHourseAgo(numHours: Long): Boolean {
        var dateDifference = Date().time - this.loadTime
        var numMilliSecondsPerHour = 3600000
        return (dateDifference < (numMilliSecondsPerHour * numHours))

    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanHourseAgo(4)
    }

    fun fetchAd() {
        if (isAdAvailable()) {
            return
        }
        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                ++adReloadCount
                if (adReloadCount < 4) {
                    fetchAd()
                } else {
//
                }
            }

            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    appOpenAd.setImmersiveMode(true)
                }
                adsStatus = adsLoaded
                adReloadCount = 0
                this@AppOpenManager.appOpenAd = appOpenAd
                this@AppOpenManager.loadTime = (Date()).time.toInt()
            }
        }
        val request = getAdRequest()
        adsStatus = adsLoading
        AppOpenAd.load(myApplication, idAds, request, loadCallback)
    }


    private fun checkTime(timeOld: Long, timeCurrent: Long): Boolean {
        return TimeUnit.MILLISECONDS.toSeconds(timeCurrent - timeOld) > currentActivity.getSharedPreferences(
            PREFS, Context.MODE_PRIVATE
        ).getInt(time_min_show_ads, 35)
    }

//    @SuppressLint("SuspiciousIndentation")
//    fun showAdIfAvailable(){
//        if (!isShowingAd && isAdAvailable() && !myApplication.isShowFullAds){
//            if (appOpenAd != null){
//                val fullScreenContentCallback = object :FullScreenContentCallback(){
//                    override fun onAdDismissedFullScreenContent() {
//                        super.onAdDismissedFullScreenContent()
//                        if (appOpenListener != null ) appOpenListener!!.onCloseOpenApp()
//                        appOpenAd =null
//                        isWifiNetworkSelectorhowingAd =false
//                        fetchAd()
//                    }
//
//                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                        super.onAdFailedToShowFullScreenContent(p0)
//                        if (appOpenListener != null) appOpenListener!!.onCloseOpenApp()
//                        fetchAd()
//                    }
//
//                    override fun onAdClicked() {
//                        super.onAdClicked()
//                    }
//
//                    override fun onAdShowedFullScreenContent() {
//                        super.onAdShowedFullScreenContent()
//                        isShowingAd =true
//                        myApplication.lastTimeForShowInterAds=System.currentTimeMillis()
//                    }
//
//                }
//                appOpenAd!!.fullScreenContentCallback =fullScreenContentCallback
//                    appOpenListener!!.onShowOpenApp()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    if (!isLeftScreen) {
//                        appOpenAd!!.show(currentActivity)
//                        myApplication.lastTimeForShowInterAds=System.currentTimeMillis()
//                    }
//                }, 100)
//            }
//        }
//    }

    fun isLoadFailed():Boolean{
        return adsStatus == adsLoadFailed
    }

    fun isNotLoad():Boolean{
        return adsStatus ==0
    }

    fun isLoaded() :Boolean{
        return adsStatus == adsLoaded
    }

    fun isLoading() :Boolean{
        return adsStatus == adsLoading
    }

    fun setCallBackActivity(activity: Activity) {
        currentActivity = activity
        if (activity is AppOpenListener) {
            setOpenAppListener(activity as AppOpenListener)
        }
    }
    fun setOpenAppListener(openAppListener: AppOpenListener?) {
        this.appOpenListener = openAppListener
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //
    }

    override fun onActivityDestroyed(activity: Activity) {
        //
    }

    override fun onActivityPaused(activity: Activity) {
        //
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        setCallBackActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //
    }

    override fun onActivityStarted(activity: Activity) {
        //
    }

    override fun onActivityStopped(activity: Activity) {
        //
    }
}