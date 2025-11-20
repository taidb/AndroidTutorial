package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.Manifest

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.eco.musicplayer.audioplayer.music.R

class Api33Activity : BasePermissionActivity() {
    private lateinit var permissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_permission)
        val debugText = """
            SDK_INT: ${Build.VERSION.SDK_INT}
            RELEASE: ${Build.VERSION.RELEASE}
            CODENAME: ${Build.VERSION.CODENAME}
            TIRAMISU: ${Build.VERSION_CODES.TIRAMISU} (API 33)
            R: ${Build.VERSION_CODES.R} (API 30)
            Current API: ${Build.VERSION.SDK_INT}
        """.trimIndent()

        println(debugText)

        // Hiển thị trên UI để dễ debug
        findViewById<TextView>(R.id.tvTitle).text = debugText

        // Khởi tạo permissions cho API 33+(version 13 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        showSimpleDialog(
            "API 33+ Behavior",
            "• Quyền Media CHI TIẾT: Ảnh, Video, Audio riêng biệt\n" +
                    "• Notification: Cần POST_NOTIFICATIONS\n" +
                    "• Từ chối 2 lần → Không hỏi lại\n"
        )

        findViewById<Button>(R.id.btnRequest).setOnClickListener { requestPermissions() }
    }

    private fun requestPermissions() {
        denyCount++

        if (denyCount >= 3) {
            showSimpleDialog(
                "Đã từ chối ${denyCount-1} lần",
                "Chuyển vào Settings."
            )
            showSettingsDialog()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            showSimpleDialog(
                "Lần yêu cầu thứ $denyCount",
                "Sắp hiển thị hộp thoại hệ thống với các quyền CHI TIẾT:\n\n" +
                        "• READ_MEDIA_IMAGES - Truy cập ảnh\n" +
                        "• READ_MEDIA_VIDEO - Truy cập video\n" +
                        "• READ_MEDIA_AUDIO - Truy cập audio\n" +
                        "• POST_NOTIFICATIONS - Hiển thị thông báo\n\n"

            )

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
//
//            val resultBuilder = StringBuilder()
//
//            permissions.forEachIndexed { index, permission ->
//                val granted = grantResults[index] == PackageManager.PERMISSION_GRANTED
//
//                val status = if (granted) " ĐƯỢC CẤP" else "BỊ TỪ CHỐI"
//
//                resultBuilder.append("$permission → $status\n")
//            }
//
//            showSimpleDialog(
//                "Kết quả cấp quyền:",
//                resultBuilder.toString()
//            )
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allGranted) {
                denyCount = 0
                showSimpleDialog(
                    "Thành công",
                    "Tất cả quyền đã được cấp!\n\n" +
                            "• Quyền media chi tiết đã được cấp\n" +
                            "• Quyền notification đã được cấp"
                )
            } else {
                showSimpleDialog(
                    "Đã từ chối $denyCount lần",
                    "Một số quyền bị từ chối. Hãy thử lại."
                )
            }
        }
    }
}