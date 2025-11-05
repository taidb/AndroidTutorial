package com.eco.musicplayer.audioplayer.music.activity.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.activity.broadcast.Broadcast
import com.eco.musicplayer.audioplayer.music.activity.broadcast.CustomBroadcast
import com.eco.musicplayer.audioplayer.music.activity.model.Staff
import com.eco.musicplayer.audioplayer.music.activity.model.Student
import com.eco.musicplayer.audioplayer.music.activity.model.Teacher
import com.eco.musicplayer.audioplayer.music.activity.network.isNetworkAvailable
import com.eco.musicplayer.audioplayer.music.activity.network.isWifeEnabled
import com.eco.musicplayer.audioplayer.music.databinding.ActivityMain4Binding
import com.google.gson.Gson

class MainActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityMain4Binding
    private lateinit var broadcast: Broadcast

    private val denialCount = mutableMapOf<String, Int>()
    private val MAX_DENIAL_COUNT = 2

    private val PREFS_NAME = "PermissionPrefs"
    private val DENIAL_COUNT_KEY = "denial_count"

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDenialCount()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        transferObject1()
        transferObject2()
        transferObject3()
        isCheckNetwork()
        navigationPage(ListStudentActivity::class.java)
        useBroacastReceiver()
        useFragment()

        binding.btnVersionSdk1.setOnClickListener {
            requestPermissionsForVersion(1)
        }
        binding.btnVersionSdk2.setOnClickListener {
            requestPermissionsForVersion(2)
        }
        binding.btnVersionSdk3.setOnClickListener {
            requestPermissionsForVersion(3)
        }
    }

    private fun isCheckNetwork() {
        binding.checkNetwork.setOnClickListener {
            when {
                isWifeEnabled(applicationContext) -> {
                    Toast.makeText(this, "WiFi turned on", Toast.LENGTH_LONG).show()
                }
                isNetworkAvailable(applicationContext) -> {
                    Toast.makeText(this, "Network is available", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun transferObject1() {
        binding.btnParcelable.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            val student = Student("Nguyen Van A", 2021, "Ha Noi")
            intent.putExtra("student", student)
            startActivity(intent)
        }
    }

    private fun transferObject2() {
        binding.btnSerializable.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            val staff = Staff(1, "Nguyen Van A", 23, 555.666)
            intent.putExtra("staff", staff)
            startActivity(intent)
        }
    }

    private fun transferObject3() {
        binding.btnTransferJson.setOnClickListener {
            val teacher = Teacher("Nguyen Van A", 46)
            val gson = Gson()
            val jsonString = gson.toJson(teacher)
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("jsonString", jsonString)
            startActivity(intent)
        }
    }

    private fun navigationPage(activityClass: Class<out Activity>) {
        binding.btnViewmodel.setOnClickListener {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun useBroacastReceiver() {
        broadcast = Broadcast()
        val intentFilter = IntentFilter().apply {
            addAction("android.intent.action.AIRPLANE_MODE")
            addAction("android.net.wifi.WIFI_STATE_CHANGED")
            addAction("android.net.wifi.STATE_CHANGE")
            addAction("android.net.conn.CONNECTIVITY_CHANGE")
            addAction("android.intent.action.ACTION_BATTERY_LOW")
            addAction("android.intent.action.ACTION_BATTERY_OKAY")
        }
        registerReceiver(broadcast, intentFilter)

        binding.btnBroadcastReceiver.setOnClickListener {
            val intent = Intent(this, CustomBroadcast::class.java)
            startActivity(intent)
            val intent2 = Intent("test.Broadcast")
            intent2.setPackage(packageName)
            sendBroadcast(intent2)
        }
    }

    private fun useFragment() {
        binding.btnFragment.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestPermissionsForVersion(version: Int) {
        val permissions = when (version) {
            1 -> getPermissionsForBelow11()
            2 -> getPermissionsFor11To12()
            3 -> getPermissionsFor13Plus()
            else -> emptyArray()
        }
        requestPermissions(permissions)
    }

    private fun getPermissionsForBelow11(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    private fun getPermissionsFor11To12(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    private fun getPermissionsFor13Plus(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        }
    }

    private fun requestPermissions(permissions: Array<String>) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (deniedPermissions.isEmpty()) {
            binding.tvStatus.text = "Tất cả quyền đã được cấp!"
            return
        }

        // Kiểm tra xem có quyền nào đã từ chối quá số lần cho phép không
        val exceededPermissions = deniedPermissions.filter { permission ->
            denialCount.getOrDefault(permission, 0) >= MAX_DENIAL_COUNT
        }

        if (exceededPermissions.isNotEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            showGoToSettingsDialog(exceededPermissions)
        } else {
            ActivityCompat.requestPermissions(this, deniedPermissions, 123)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) {
            handlePermissionResult(permissions, grantResults)
        }
    }

    private fun handlePermissionResult(permissions: Array<out String>, grantResults: IntArray) {
        val deniedPermissions = mutableListOf<String>()

        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission)
                // Tăng số lần từ chối
                denialCount[permission] = denialCount.getOrDefault(permission, 0) + 1
            } else {
                // Reset số lần từ chối nếu được cấp quyền
                denialCount.remove(permission)
            }
        }

        saveDenialCount()

        if (deniedPermissions.isEmpty()) {
            binding.tvStatus.text = "Tất cả quyền đã được cấp!"
        } else {
            val exceededPermissions = deniedPermissions.filter { permission ->
                denialCount.getOrDefault(permission, 0) >= MAX_DENIAL_COUNT
            }

            if (exceededPermissions.isNotEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                showGoToSettingsDialog(exceededPermissions)
            } else {
                showRetryDialog(deniedPermissions)
            }
        }
    }

    private fun showRetryDialog(deniedPermissions: List<String>) {
        AlertDialog.Builder(this)
            .setTitle("Quyền bị từ chối")
            .setMessage("Bạn có muốn thử lại không?")
            .setPositiveButton("Thử lại") { _, _ ->
                ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), 123)
            }
            .setNegativeButton("Hủy") { _, _ ->
                binding.tvStatus.text = "Đã hủy cấp quyền"
            }
            .show()
    }

    private fun showGoToSettingsDialog(permissions: List<String>) {
        AlertDialog.Builder(this)
            .setTitle("Quyền bị từ chối nhiều lần")
            .setMessage("Bạn đã từ chối quyền nhiều lần. Vui lòng cấp quyền trong cài đặt!")
            .setPositiveButton("Mở cài đặt") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Hủy") { _, _ ->
                binding.tvStatus.text = "Quyền bị từ chối vĩnh viễn"
            }
            .show()
    }

    private fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Không thể mở cài đặt", Toast.LENGTH_SHORT).show()
        }
    }

    // Lưu và tải số lần từ chối từ SharedPreferences
    private fun loadDenialCount() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val denialCountString = prefs.getString(DENIAL_COUNT_KEY, "")
        denialCountString?.split(";")?.forEach { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) {
                denialCount[parts[0]] = parts[1].toInt()
            }
        }
    }

    private fun saveDenialCount() {
        val stringBuilder = StringBuilder()
        denialCount.forEach { (permission, count) ->
            stringBuilder.append("$permission:$count;")
        }
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
            .putString(DENIAL_COUNT_KEY, stringBuilder.toString())
            .apply()
    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(broadcast)
        } catch (e: Exception) {
        }
    }
}