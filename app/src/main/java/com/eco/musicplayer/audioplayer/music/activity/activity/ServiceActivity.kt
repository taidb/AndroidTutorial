package com.eco.musicplayer.audioplayer.music.activity.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.activity.service.LocationForegroundService
import com.eco.musicplayer.audioplayer.music.activity.service.MyBackgroundService
import com.eco.musicplayer.audioplayer.music.activity.service.MyBoundService
import com.eco.musicplayer.audioplayer.music.activity.service.MyForegroundService
import com.eco.musicplayer.audioplayer.music.databinding.ActivityServiceBinding


class ServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceBinding
    private val loctationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startLocationService()
        else Toast.makeText(this, "Quyền bị từ chối", Toast.LENGTH_SHORT).show()
    }
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

    fun checkPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationService()
        } else {
            loctationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent)
            binding.txtBoundData.text = "Location Foreground Service Start"
        } else {
            startService(intent)
        }
    }

    @SuppressLint("ImplicitSamInstance")
    fun stopLocationService() {
        stopService(Intent(this, LocationForegroundService::class.java))
        binding.txtBoundData.text = "Location Foreground Service stopped"
    }

    fun startForegroundService() {
        val intent = Intent(this, MyForegroundService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ yêu cầu quyền thông báo
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
                return
            }
            binding.txtBoundData.text = "Foreground Service Start"
            // Bắt buộc dùng startForegroundService
            ContextCompat.startForegroundService(this, intent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8 - 12
            ContextCompat.startForegroundService(this, intent)
        } else {
            // Android 7 trở xuống
            startService(intent)
        }
    }

    fun stopForegroundService() {
        Log.d("ServiceActivity", "Stopping Foreground Service")
        stopService(Intent(this, MyForegroundService::class.java))
        binding.txtBoundData.text = "Foreground Service stopped"
    }

    fun startBackgroundService() {
        Log.d("ServiceActivity", "Starting Background Service")
        startService(Intent(this, MyBackgroundService::class.java))
        binding.txtBoundData.text = "Background Service started"
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