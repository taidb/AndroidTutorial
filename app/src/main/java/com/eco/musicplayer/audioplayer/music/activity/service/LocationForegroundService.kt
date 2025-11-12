package com.eco.musicplayer.audioplayer.music.activity.service
import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.activity.activity.MainActivity4
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationForegroundService : Service() {
    private lateinit var fusedLocationClient :FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val CHANNEL_ID ="Location_service_channel"
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient =LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Đang theo dõi vị trí...")
        startForeground(1, notification)
        startLocationUpdates()
        return START_STICKY
    }


    //quyền ACCESS_FINE_LOCATION cho phép ứng dụng truy cập vị trí của thiết bị với độ chính xác cao, sử dụng các phương pháp định vị như GPS hoặc vệ tinh
    private fun startLocationUpdates(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            stopSelf()
            return
        }

        val locationRequest =LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,3000L
        ).build()
        locationCallback =object  :LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location in p0.locations){
                    updateNotification(location)
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,mainLooper)
    }

    private fun updateNotification(location:Location){
        val text ="Lat ${location.latitude} ,Lng: ${location.longitude}"
        val notification =createNotification(text)
        val nm =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1,notification)
    }

    private fun createNotification(text: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity4::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Đang theo dõi vị trí")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Location Tracking Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
        }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? =null
}