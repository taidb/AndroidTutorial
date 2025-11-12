package com.eco.musicplayer.audioplayer.music.activity.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.eco.musicplayer.audioplayer.music.activity.ads.PREFS

val Context.getSharedPreferences: SharedPreferences
    get() = getSharedPreferences(
        PREFS,
        MODE_PRIVATE
    )

@SuppressLint("UseKtx")
fun Context.putShared(key: String, value: Any) {
    val edit = getSharedPreferences.edit()
    when (value) {
        is String -> {
            edit.putString(key, value)
        }

        is Int -> {
            edit.putInt(key, value)
        }

        is Boolean -> {
            edit.putBoolean(key, value)
        }

        is Float -> {
            edit.putFloat(key, value)
        }

        is Long -> {
            edit.putLong(key, value)
        }
    }
    edit.apply()
}

fun Context.getSharedString(key: String, default: String = ""): String {
    if (this == null) {
        return " "
    }
    return getSharedPreferences.getString(key,default) ?:default
}
fun Context?.getSharedInt(key: String ,default: Int =-1):Int{
    if (this ==null){
        return -1
    }
    return getSharedPreferences.getInt(key,default)
}

fun Context?.getSharedLong(key: String, default: Long = 0): Long {
    if (this == null) {
        return -1
    }
    return getSharedPreferences.getLong(key, default)
}

fun Context?.getSharedFloat(key: String): Float {
    if (this == null) {
        return 0.5f
    }
    return getSharedPreferences.getFloat(key, 0.5f)
}

fun Context?.getSharedBoolean(key: String, default: Boolean = false): Boolean {
    if (this == null) {
        return true
    }
    return getSharedPreferences.getBoolean(key, default)
}

fun Context?.contains(key: String) : Boolean {
    if (this == null) {
        return false
    }
    return getSharedPreferences.contains(key)
}