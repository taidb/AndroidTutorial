package com.eco.musicplayer.audioplayer.music.activity.service


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyBackgroundService : Service() {

    override fun onCreate() { //được gọi khi service lần đầu tiên được tạo.Thường dùng để khởi tạo các tài nguyên ví dụ: mở kết nối database, tạo thread, hoặc set up listener.
        super.onCreate()
        Log.d("MyBackgroundService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { //được gọi mỗi khi Activity hoặc component khác gọi startService().
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

    override fun onDestroy() { //Được gọi khi service bị dừng hoặc stopSelf() được gọi.
        super.onDestroy()
        Log.d("MyBackgroundService", "onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
