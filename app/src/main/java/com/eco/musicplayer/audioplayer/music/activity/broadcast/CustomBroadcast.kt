package com.eco.musicplayer.audioplayer.music.activity.broadcast

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.music.R
//Thường được sử dụng để truyền thông điệp trong và ngoài ứng dụng nhưng không phải là sự thay đổi
// từ hệ thống mà là những thông điệp mà lập trình viên muốn truyền đi.Nó thường được sự dụng cho các
// trường hợp: thông báo dữ liệu đã được tải xong,khi 1 ứng dụng cần gửi tin nhắn đến ứng dụng khác,
//liên lạc giữa các thành phần trong cùng một ứng dụng
//cập nhật UI từ một tiến trình nền
class CustomBroadcast : AppCompatActivity() {
    private lateinit var broadcast:Broadcast
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_broadcast)
        broadcast=Broadcast()
        val intentFilter = IntentFilter("test.Broadcast")
        registerReceiver(broadcast, intentFilter)

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcast);
    }
}