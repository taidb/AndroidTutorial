package com.eco.musicplayer.audioplayer.music.activity.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

class PermissionManager(activity: Activity) {
    // WeakReference để tránh memory leak
    private val activityRef = WeakReference(activity)
    companion object {
        // Danh sách các quyền thường dùng
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        const val READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val PHONE_PERMISSION = Manifest.permission.CALL_PHONE
        const val SMS_PERMISSION = Manifest.permission.SEND_SMS
        const val CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS

        const val REQUEST_CODE = 1001

        // Singleton theo từng activity
        fun from(activity: Activity): PermissionManager {
            return PermissionManager(activity)
        }
    }

    // Kiểm tra xem quyền đã được cấp chưa
//    fun hasPermission(permission: String): Boolean {
//        val activity = activityRef.get() ?: return false
//        return ContextCompat.checkSelfPermission(
//            activity,
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }

    // Xin một hoặc nhiều quyền
    fun requestPermissions(vararg permissions: String) {
        val activity = activityRef.get() ?: return
        val neededPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (neededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, neededPermissions.toTypedArray(), REQUEST_CODE)
        }
    }

    // Kiểm tra kết quả sau khi người dùng chọn cho phép/từ chối
    fun handleRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onGranted: (() -> Unit)? = null,
        onDenied: ((denied: List<String>) -> Unit)? = null
    ) {
        if (requestCode == REQUEST_CODE) {
            val deniedPermissions = mutableListOf<String>()
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i])
                }
            }
            if (deniedPermissions.isEmpty()) onGranted?.invoke()
            else onDenied?.invoke(deniedPermissions)
        }
    }
}
