package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.content.Intent
import android.net.Uri

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.provider.Settings


abstract class BasePermissionActivity : AppCompatActivity() {
    protected companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    protected var denyCount = 0

    protected fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    protected fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cần cấp quyền thủ công")
            .setMessage("Bạn đã từ chối quyền nhiều lần. Vui lòng cấp quyền trong Settings để tiếp tục sử dụng ứng dụng.")
            .setPositiveButton("Đến Settings") { _, _ -> goToSettings() }
            .setNegativeButton("Hủy", null)
            .show()
    }

    protected fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }
}