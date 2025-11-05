package com.eco.musicplayer.audioplayer.music.activity.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlin.random.Random

class MyBoundService : Service() {

    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MyBoundService = this@MyBoundService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MyBoundService", "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("MyBoundService", "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("MyBoundService", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyBoundService", "onDestroy")
    }

    fun getRandomNumber(): Int {
        return Random.nextInt(0, 100)
    }
}
