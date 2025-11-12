package com.eco.musicplayer.audioplayer.music.activity

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.eco.musicplayer.audioplayer.music.activity.ads.APP_OPEN_ID
import com.eco.musicplayer.audioplayer.music.activity.ads.AppOpenManager
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Context.getApplication(result: ((CompressVideoApplication) ->Unit) ?=null){
    if(applicationContext is CompressVideoApplication){
        result?.let { it(applicationContext as CompressVideoApplication) }
    }
}

class CompressVideoApplication :Application(),Application.ActivityLifecycleCallbacks {
    var isShowFullAds =false
    var isShowAdsWhenOpen =false
    var lastTimeForShowInterAds =0L
    var remoteConfig =false
    var isShowedOfferDialog =false
    override fun onCreate() {
        super.onCreate()
        val appOpenManager =AppOpenManager(this,APP_OPEN_ID)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
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