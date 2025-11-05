package com.eco.musicplayer.audioplayer.music.activity.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class Broadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val action = intent?.action
        var message = ""

        when (action) {
            "android.intent.action.AIRPLANE_MODE" -> {
                message = "Air Plane mode changed"
            }
            "android.intent.action.ACTION_BATTERY_LOW" -> {
                message = "ACTION_BATTERY_LOW"
            }
            "android.intent.action.ACTION_BATTERY_LOW" -> {
                message = "ACTION_BATTERY_LOW"
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
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}