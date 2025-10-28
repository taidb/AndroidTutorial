package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.activity.util.PermissionManager
import com.eco.musicplayer.audioplayer.music.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionManager = PermissionManager.from(this)
        binding.btnCamera.setOnClickListener {
            permissionManager.requestPermissions(PermissionManager.CAMERA_PERMISSION)
        }

        binding.btnStorage.setOnClickListener {
            permissionManager.requestPermissions(
                PermissionManager.READ_STORAGE_PERMISSION,
                PermissionManager.WRITE_STORAGE_PERMISSION
            )
        }

        binding.btnPhone.setOnClickListener {
            permissionManager.requestPermissions(PermissionManager.PHONE_PERMISSION)
        }

        binding.btnSms.setOnClickListener {
            permissionManager.requestPermissions(PermissionManager.SMS_PERMISSION)
        }

        binding.btnContacts.setOnClickListener {
            permissionManager.requestPermissions(PermissionManager.CONTACTS_PERMISSION)
        }
    }

    // Xử lý kết quả xin quyền
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.handleRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            onGranted = {
                Toast.makeText(this, "Tất cả quyền đã được cấp!", Toast.LENGTH_SHORT).show()
            },
            onDenied = { denied ->
                Toast.makeText(this, "Các quyền bị từ chối: $denied", Toast.LENGTH_SHORT).show()
            }
        )
    }
}