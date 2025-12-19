package com.eco.musicplayer.audioplayer.music.activity.activity

import android.app.Application
import com.eco.musicplayer.audioplayer.music.activity.adsme.AdManager

class AdDemoApp : Application() {

    lateinit var adManager: AdManager

    override fun onCreate() {
        super.onCreate()
        adManager = AdManager(this)
    }
}