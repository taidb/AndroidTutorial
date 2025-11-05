package com.eco.musicplayer.audioplayer.music.activity.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.service.MyBackgroundService
import com.eco.musicplayer.audioplayer.music.activity.service.MyBoundService
import com.eco.musicplayer.audioplayer.music.activity.service.MyForegroundService
import com.eco.musicplayer.audioplayer.music.databinding.ActivityServiceBinding


class ServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceBinding

    private var boundService: MyBoundService? = null
    private var isBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBoundService.MyBinder
            boundService = binder.getService()
            isBound = true
            Log.d("ServiceActivity", "Bound Service connected")
            binding.txtBoundData.text = "BoundService trả về: ${boundService?.getRandomNumber()}"
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            boundService = null
            Log.d("ServiceActivity", "Bound Service disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServiceBinding.inflate(layoutInflater)
        binding.activity = this
        setContentView(binding.root)

        // Test log ngay khi vào activity
        Log.d("ServiceActivity", "Activity created")
    }

    fun startForegroundService() {
        Log.d("ServiceActivity", "Starting Foreground Service")
        val intent = Intent(this, MyForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
        binding.txtBoundData.text = "Foreground Service started"
    }

    fun stopForegroundService() {
        Log.d("ServiceActivity", "Stopping Foreground Service")
        stopService(Intent(this, MyForegroundService::class.java))
        binding.txtBoundData.text = "Foreground Service stopped"
    }

    fun startBackgroundService() {
        Log.d("ServiceActivity", "Starting Background Service")
        startService(Intent(this, MyBackgroundService::class.java))
        binding.txtBoundData.text = "Background Service started - Check logcat in 5 seconds"
    }

    fun stopBackgroundService() {
        Log.d("ServiceActivity", "Stopping Background Service")
        stopService(Intent(this, MyBackgroundService::class.java))
        binding.txtBoundData.text = "Background Service stopped"
    }

    fun bindMyService() {
        Log.d("ServiceActivity", "Binding Service")
        val intent = Intent(this, MyBoundService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun unbindMyService() {
        Log.d("ServiceActivity", "Unbinding Service")
        if (isBound) {
            unbindService(connection)
            isBound = false
            binding.txtBoundData.text = "Service đã unbind"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Tự động unbind khi activity bị hủy
        unbindMyService()
        Log.d("ServiceActivity", "Activity destroyed")
    }
}