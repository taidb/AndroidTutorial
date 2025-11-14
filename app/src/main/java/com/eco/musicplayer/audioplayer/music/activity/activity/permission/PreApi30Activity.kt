package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button

import com.eco.musicplayer.audioplayer.music.R
//Trước Android 11 (API < 30)	Hộp thoại yêu cầu quyền của hệ thống sẽ luôn hiển thị mỗi
// lần bạn yêu cầu, trừ khi người dùng chọn rõ ràng tùy chọn "Don't ask again" (Không hỏi lại).
//Android 11 trở lên (API >= 30)	Nếu người dùng nhấn "Từ chối" hai lần (không có hộp kiểm
// "Don't ask again" hiển thị nữa), hệ thống sẽ ngầm hiểu là "không hỏi lại".
// Các yêu cầu tiếp theo sẽ tự động bị từ chối ngay lập tức mà không hiển thị hộp thoại hệ thống.
// Truy cập bộ nhớ/ảnh/video/audio:
//Trước Android 13: Sử dụng quyền chung READ_EXTERNAL_STORAGE hoặc WRITE_EXTERNAL_STORAGE.
//Android 13 (API 33) trở lên: Cần các quyền chi tiết hơn như READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_AUDIO.
// Điều này giúp người dùng chỉ cấp quyền truy cập vào loại tệp cụ thể mà ứng dụng cần.
//Thông báo (Notifications):
//Trước Android 13: Quyền thông báo được cấp mặc định khi cài đặt.
//Android 13 (API 33) trở lên: Yêu cầu quyền thời gian chạy POST_NOTIFICATIONS. Người dùng có thể từ chối quyền này.
//Vị trí, Camera, Microphone: Android 11 giới thiệu quyền một lần (one-time permissions),
//Android 14:chỉ cho phép người dùng chia sẻ 1 số ảnh/video với app :
//Người dùng sẽ thấy 3 tùy chọn:
//Cho phép tất cả ảnh
//Cho phép chỉ một số ảnh
//Không cho phép
//Android 15: Foreground Service chỉ được phép chạy tối đa 6 tiếng
//Một số quyền liên quan đến truy cập background location (ACCESS_BACKGROUND_LOCATION) yêu cầu người dùng đồng ý foreground trước.
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