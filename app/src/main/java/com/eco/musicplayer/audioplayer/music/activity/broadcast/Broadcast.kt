package com.eco.musicplayer.audioplayer.music.activity.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
//BroadcastReceiver chỉ sống tạm thời, không nên thực hiện công việc nặng
// (như tải dữ liệu, truy cập mạng) bên trong nó.
// có 2 loại : Static Receiver Hoạt động ngay cả khi app chưa chạy: Khai báo trong AndroidManifest.xml
//Dynamic Receiver Chỉ hoạt động khi app đang chạy Đăng ký trong code
class Broadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val action = intent.action
        var message = ""

        when (action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED
          -> {
                message = "Air Plane mode changed"
            }
            Intent.ACTION_BATTERY_LOW
           -> {
                message = "ACTION_BATTERY_LOW"
            }
            Intent.ACTION_BATTERY_OKAY
        -> {
                message = "ACTION_BATTERY_OKAY"
            }

            "android.net.wifi.WIFI_STATE_CHANGED" -> {
                message = "WIFI_STATE_CHANGED"
            }
            "android.net.wifi.STATE_CHANGE" -> {
                message = "STATE_CHANGE"
            }
            "android.net.conn.CONNECTIVITY_CHANGE" -> {
                message = "CONNECTIVITY_CHANGE"
            }
            "test.Broadcast" -> {
                message = "Nhận được custom broadcast!"
            }
        }

        if (message.isNotEmpty()) {
            printToast(context, message)
        }

    }

    private fun printToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}