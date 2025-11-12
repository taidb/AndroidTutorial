package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button

import com.eco.musicplayer.audioplayer.music.R
class PreApi30Activity : BasePermissionActivity() {
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_permission)


        showSimpleDialog(
            "API < 30 Behavior",
            "• Hộp thoại hệ thống LUÔN hiển thị mỗi lần yêu cầu\n" +
                    "• Có checkbox 'Don't ask again' RÕ RÀNG\n" +
                    "• Storage: READ_EXTERNAL_STORAGE\n" +
                    "• Notification: Tự động được cấp"
        )

        findViewById<Button>(R.id.btnRequest).setOnClickListener { requestPermissions() }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

            if (allGranted) {
                showSimpleDialog("Thành công", "Tất cả quyền đã được cấp!")
            } else {
                denyCount++

                // Kiểm tra xem user có chọn "Don't ask again" không
                val hasPermanentlyDenied = permissions.any { permission ->
                    !shouldShowRequestPermissionRationale(permission)
                }

                if (hasPermanentlyDenied) {
                    showSimpleDialog(
                        "Đã chọn 'Don't ask again'",
                        "Bạn đã chọn 'Don't ask again'. Hệ thống sẽ KHÔNG hiển thị hộp thoại nữa."
                    )
                    showSettingsDialog()
                } else {
                    showSimpleDialog(
                        "Từ chối lần $denyCount",
                        "Bạn có thể yêu cầu lại. Hộp thoại hệ thống sẽ tiếp tục hiển thị."
                    )
                }
            }
        }
    }
}