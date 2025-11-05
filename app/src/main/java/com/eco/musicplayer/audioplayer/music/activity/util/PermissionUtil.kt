package com.eco.musicplayer.audioplayer.music.activity.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
const val REQUEST_NOTIFICATION = 1001
//Android 10 trở xuống: Quyền truy cập bộ nhớ được cấp thông qua quyền READ_EXTERNAL_STORAGE.
// Người dùng có thể cấp hoặc từ chối quyền này cho các tệp phương tiện (ảnh, video, audio).
//Android 11: Google đã giới thiệu các biện pháp bảo mật mới.
//Quyền một lần: Hộp thoại cấp quyền có thêm tùy chọn "Chỉ lần này", cho phép người dùng cấp quyền sử dụng tạm thời.
//Quyền truy cập bộ nhớ được giới hạn: Ứng dụng có thể chỉ truy cập vào ảnh, video và âm thanh mà ứng dụng đó đã tạo.
// Để truy cập các tệp của ứng dụng khác, ứng dụng phải sử dụng API MediaStore hoặc yêu cầu quyền READ_EXTERNAL_STORAGE.
//Android 12: Tiếp tục cải thiện các chính sách bảo mật, cho phép người dùng kiểm soát quyền truy cập vào micrô và camera,
// thông qua việc sử dụng các tính năng như "Chế độ riêng tư" và "Thanh trạng thái quyền truy cập".
//Android 13 trở lên:
//Quyền truy cập thông báo: Các ứng dụng giờ đây phải yêu cầu quyền truy cập thông báo của người dùng trước khi có thể gửi thông báo.
//Quyền truy cập phương tiện: Cung cấp quyền truy cập chi tiết hơn vào các loại phương tiện,
// cho phép người dùng chọn chỉ cấp quyền cho ảnh, video hoặc âm thanh mà không cần cấp quyền truy cập tất cả các tệp.
class PermissionUtil(private val context: Context) {

    val permissionsStorage: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }


    val permissionsVideo: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }

    fun requestWriteStorage(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity, permissionsStorage, requestCode
        )
    }


    fun isHasPermissionLimit(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }


    fun isHasPermissionVideo(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun isHasPermissionStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val videoPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO)
            val allPhotosPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            return videoPermission == PackageManager.PERMISSION_GRANTED &&
                    allPhotosPermission == PackageManager.PERMISSION_GRANTED
        } else {
            for (permission in permissionsStorage) {
                if (ActivityCompat.checkSelfPermission(
                        context, permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun isHasNotification(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (permission in arrayOf(Manifest.permission.POST_NOTIFICATIONS)) {
                if (ContextCompat.checkSelfPermission(
                        context, permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                }
            }
            return false
        }
        return true
    }


    fun allowCamera(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, "android.permission.CAMERA"
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun requestPermissionsVideo(
        activity: Activity,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(activity, permissionsVideo, requestCode)
    }

    fun requestNotification(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION)
            }
        }
    }
}