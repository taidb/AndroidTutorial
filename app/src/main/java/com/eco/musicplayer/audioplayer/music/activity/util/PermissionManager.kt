import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager {

    companion object {
        // Các quyền theo phiên bản Android
        fun getStoragePermissions(): Array<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ - Quyền chi tiết
                arrayOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                // Android < 13 - Quyền chung
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        fun getCameraPermissions(): Array<String> {
            return arrayOf(android.Manifest.permission.CAMERA)
        }

        fun getAudioPermissions(): Array<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                arrayOf(android.Manifest.permission.RECORD_AUDIO)
            }
        }

        fun getNotificationPermission(): Array<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                arrayOf() // Không cần quyền runtime trước Android 13
            }
        }

        // Kiểm tra xem đã từ chối quyền nhiều lần chưa
        fun shouldShowRequestPermissionRationale(activity: Activity, permissions: Array<String>): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    return true
                }
            }
            return false
        }

        // Kiểm tra xem đã bị từ chối vĩnh viễn chưa
        fun isPermanentlyDenied(activity: Activity, permissions: Array<String>): Boolean {
            for (permission in permissions) {
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                val isDenied = ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED

                if (isDenied && !shouldShowRationale) {
                    return true
                }
            }
            return false
        }

        // Mở cài đặt app
        fun openAppSettings(activity: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
            }
            activity.startActivity(intent)
        }
    }
}