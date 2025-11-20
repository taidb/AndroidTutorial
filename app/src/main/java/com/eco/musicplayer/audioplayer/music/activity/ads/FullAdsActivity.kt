package com.eco.musicplayer.audioplayer.music.activity.ads

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eco.musicplayer.audioplayer.music.activity.AdsApplication
import com.eco.musicplayer.audioplayer.music.activity.ads.inter.InterstitialListener
import com.eco.musicplayer.audioplayer.music.activity.ads.inter.InterstitialUtil
import com.eco.musicplayer.audioplayer.music.activity.designlayout.PaywallActivity
import com.eco.musicplayer.audioplayer.music.activity.getApplication
import com.eco.musicplayer.audioplayer.music.databinding.ActivityFullAdsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullAdsActivity : AppCompatActivity(),OpenListener {
    private val binding by lazy { ActivityFullAdsBinding.inflate(layoutInflater) }
    var isFirstOpen = false
    val appOpenSplashManager by lazy { AppOpenSplashManager(this) }
    val onboardInterstitiaUtil by lazy { InterstitialUtil() }
    var countTime = 0
    var limitTime = 3
    var jobLoadAds: Job? = null
    val jobLoadAdsSplash: JobLoadAds by lazy { JobLoadAds() }
    private var progressCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (onboardInterstitiaUtil.isLoaded().not() && onboardInterstitiaUtil.isLoading().not()) {
            onboardInterstitiaUtil.loadAds(this)
        }
        appOpenSplashManager.fetchAd(OPEN_SPLASH)
       jobLoadAdsSplash.startJob {
           if(!isActive()){
               return@startJob
           }
           if (appOpenSplashManager.isShowingAd){
               return@startJob
           }
           jobLoadAdsSplash.setDelay(100)
           if (appOpenSplashManager.isLoaded()){
               showAdMobAds()
               return@startJob
           }
       }
    }

    fun isActive(): Boolean {
        return !isFinishing && !isDestroyed
    }

    fun showOnboardInter(activity: AppCompatActivity, listenter: InterstitialListener? = null) {
        if (onboardInterstitiaUtil.isLoaded()) {
            onboardInterstitiaUtil.listener = listenter
            onboardInterstitiaUtil.showAd(activity)
        } else listenter?.onAdFailToShow("Ad load error")
    }

//    private fun showInterAds() {
//        showOnboardInter(this, object : InterstitialListener() {
//            override fun onAdShowedFullScreen() {
//                super.onAdShowedFullScreen()
//                skip()
//                onboardInterstitiaUtil.clear()
//            }
//
//            override fun onAdFailToShow(error: String) {
//                super.onAdFailToShow(error)
//                skip()
//            }
//
//            override fun onEcoFullScreenAdsResume() {
//                super.onEcoFullScreenAdsResume()
//                //
//            }
//
//            override fun onEcoRemoveAllAdsClicked() {
//                super.onEcoRemoveAllAdsClicked()
//               //
//            }
//        )
//        }
//
//    }
//    private fun skip(isFromAppOpen :Boolean =false){
//        jobLoadAdsSplash.stopJob()
//
//    }

    private fun showAdMobAds(){
        jobLoadAdsSplash.stopJob()
        getApplication{
            val isShow =appOpenSplashManager.showAd(this)
            if (isShow){
                (application as AdsApplication).isShowFullAds=true
                (application as AdsApplication).isShowAdsWhenOpen=true

            }else{
                //
            }
        }
    }

    override fun onCloseOpenApp() {
        TODO("Not yet implemented")
    }

    override fun onOpenAppLoadFailed() {
        TODO("Not yet implemented")
    }

    override fun onEcoFullscreenAdsResume() {
        TODO("Not yet implemented")
    }

    override fun onEcoCrossRemoveAllAdsClicked() {
        TODO("Not yet implemented")
    }

}