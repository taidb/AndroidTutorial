package com.eco.musicplayer.audioplayer.music.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.UserRepository
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.appModule
import com.eco.musicplayer.audioplayer.music.activity.ads.APP_OPEN_ID
import com.eco.musicplayer.audioplayer.music.activity.ads.AppOpenManager
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.mp.KoinPlatform.getKoin

fun Context.getApplication(result: ((AdsApplication) ->Unit) ?=null){
    if(applicationContext is AdsApplication){
        result?.let { it(applicationContext as AdsApplication) }
    }
}

class AdsApplication :Application(),Application.ActivityLifecycleCallbacks {
    var isShowFullAds =false
    var isShowAdsWhenOpen =false
    var lastTimeForShowInterAds =0L
    var remoteConfig =false
    var isShowedOfferDialog =false
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()
        val appOpenManager =AppOpenManager(this,APP_OPEN_ID)
     FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG

        startKoin {
            androidContext(this@AdsApplication)
            androidLogger(Level.INFO) //debug khi cần xem dependency nào được tạo.
            modules(appModule)

            chekDependencies()
        }
    }

    private fun chekDependencies() {
        try {
            getKoin().get<UserRepository>()

        }catch (e:Exception){
             println("koin setup thất bại :${e.message}")
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityPaused(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(activity: Activity) {
        TODO("Not yet implemented")
    }
}