package com.eco.musicplayer.audioplayer.music.activity

import android.app.Application
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.UserRepository
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.mp.KoinPlatform.getKoin

class MyApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
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
}