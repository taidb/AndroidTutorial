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


        showSimpleDialog(
            "API 30-32 Behavior",
            "•Từ chối 2 lần → hệ thống: 'không hỏi lại'\n" +
                    "• KHÔNG có checkbox 'Don't ask again'\n" +
                    "• Storage: MANAGE_EXTERNAL_STORAGE\n" +
                    "• Notification: Tự động được cấp"
        )

        findViewById<Button>(R.id.btnRequest).setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        denyCount ++
        if (denyCount>=2){
            showSimpleDialog(
                "Đã từ chối $denyCount lần",
                "Hệ thống sẽ KHÔNG hiển thị hộp thoại nữa. Phải vào Settings."
            )
            showSettingsDialog()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (shouldShowRequestPermissionRationale(permissions[0])) {
//                // Đã từ chối ít nhất 1 lần trước đó
//                showSimpleDialog(
//                    "Lần yêu cầu thứ ${denyCount + 1}",
//                    "Tiếp tục từ chối, hệ thống sẽ KHÔNG hiển thị hộp thoại nữa.\n\n" +
//                            "Sẽ chuyển đến Settings để cấp quyền thủ công."
//                )
//            }
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
                denyCount = 0
                showSimpleDialog("Thành công", "Tất cả quyền đã được cấp!")
            } else {
                denyCount++
                // Kiểm tra xem có nên hiển thị giải thích không
                val shouldShowRationale = permissions.any { permission ->
                    shouldShowRequestPermissionRationale(permission)
                }

                if (!shouldShowRationale && denyCount >= 2) {
                    showSimpleDialog(
                        "Đã từ chối $denyCount lần",
                        "Hệ thống sẽ KHÔNG hiển thị hộp thoại nữa. Phải vào Settings."
                    )
                    showSettingsDialog()
                } else {
                    showSimpleDialog(
                        "Đã từ chối $denyCount lần",
                        "Hãy tiếp tục thử để thấy sự khác biệt về hành vi."
                    )
                }
            }
        }
    }
}