package com.eco.musicplayer.audioplayer.music.activity.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build

//fun isNetworkAvailables(): Boolean {
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//    val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//    return if (wifi != null) {
//        if (wifi.isConnected) true else {
//            mobile?.isConnected ?: false
//        }
//    } else {
//        mobile?.isConnected ?: false
//    }
//}

    //Kiểm tra kết nối mạng chung ( c kết nối hay không)
    fun isNetworkAvailable(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    // Kiểm tra wifi có bật không
    fun isWifeEnabled(context: Context):Boolean{
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    //Kiểm tra kết nối mạng hiện tại có thực sự cung cấp quyền truy cập vào internet hay không
    fun isInternetConnectionAvailable(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork  = connectivityManager.getNetworkCapabilities(network) ?: return false
            activeNetwork.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }else{
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI
        }
    }

