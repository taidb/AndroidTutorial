package com.eco.musicplayer.audioplayer.music.activity.service


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyBackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("MyBackgroundService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyBackgroundService", "onStartCommand - chạy nền")
        Thread {
            for (i in 1..5) {
                Log.d("MyBackgroundService", "Đang chạy tác vụ $i")
                Thread.sleep(1000)
            }
            stopSelf()
        }.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyBackgroundService", "onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
