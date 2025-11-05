package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button

import com.eco.musicplayer.audioplayer.music.R

class Api30To32Activity : BasePermissionActivity() {
    private val permissions = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_permission)

        title = "API 30-32 - Từ chối 2 lần → Không hỏi lại"

        showSimpleDialog(
            "API 30-32 Behavior",
            "• Từ chối 2 lần → hệ thống NGẦM HIỂU 'không hỏi lại'\n" +
                    "• KHÔNG có checkbox 'Don't ask again' rõ ràng\n" +
                    "• Storage: MANAGE_EXTERNAL_STORAGE\n" +
                    "• Notification: Tự động được cấp"
        )

        findViewById<Button>(R.id.btnRequest).setOnClickListener { requestPermissions() }
    }

    private fun requestPermissions() {
        denyCount++

        if (denyCount >= 3) {
            showSimpleDialog(
                "Đã từ chối ${denyCount-1} lần",
                "Hệ thống sẽ KHÔNG hiển thị hộp thoại nữa. Phải vào Settings."
            )
            showSettingsDialog()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (denyCount == 1) {
                // Lần đầu - hiển thị hộp thoại hệ thống ngay
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            } else {
                // Lần thứ 2 trở đi - hiển thị cảnh báo trước
                showSimpleDialog(
                    "Lần yêu cầu thứ $denyCount",
                    "Nếu bạn từ chối lần này, hệ thống sẽ KHÔNG hiển thị hộp thoại nữa.\n\n" +
                            "Sẽ chuyển đến Settings để cấp quyền thủ công."
                )
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
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
                denyCount = 0
                showSimpleDialog("Thành công", "Tất cả quyền đã được cấp!")
            } else {
                showSimpleDialog(
                    "Đã từ chối $denyCount lần",
                    "Hãy tiếp tục thử để thấy sự khác biệt về hành vi."
                )
            }
        }
    }
}