package com.eco.musicplayer.audioplayer.music.activity.service

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.music.R

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.eco.musicplayer.audioplayer.music.activity.activity.MainActivity4
import com.eco.musicplayer.audioplayer.music.activity.activity.ServiceActivity

//Không nên lạm dụng Foreground Service cho tác vụ ngắn (vì nó chiếm tài nguyên hệ thống).
//Nếu chỉ cần chạy định kỳ — nên dùng WorkManager.
class MyForegroundService : Service() {

    private val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onCreate() { // Được gọi khi service lần đầu tiên được tạo.
        super.onCreate()
        Log.d("MyForegroundService", "onCreate")
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int { //Được gọi mỗi khi service được startService() từ Activity.
        Log.d("MyForegroundService", "onStartCommand")

        createNotificationChannel()
        val notification=createNotification()

        startForeground(1, notification) //đưa service lên foreground
        return START_STICKY //Tự động khởi động lại service khi có tài nguyên
        // START_NOT_STICKY:Không khởi động lại sau khi bị kill (phổ biến)
        //START_REDELIVER_INTENT Khởi động lại và gửi lại Intent cũ
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, ServiceActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service đang chạy")
            .setContentText("Ứng dụng đang hoạt động nền")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()

    }


    override fun onDestroy() { //Gọi khi service bị stop hoặc app bị kill.
        super.onDestroy()
        Log.d("MyForegroundService", "onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? =
        null //Service này không hỗ trợ bind → trả về null.

    // Từ Android 8.0 (API 26, Oreo) trở lên, Android bắt buộc mọi Notification phải thuộc về một kênh thông báo (NotificationChannel).
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}