package com.eco.musicplayer.audioplayer.music.activity.ads

import android.content.Context
import androidx.startup.Initializer
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//FirebaseCrashlytics dùng để báo cáo và quản lý các sự cố, lỗi xảy ra trong ứng dụng 1 cách tự động.
//Nó giúp theo dõi và nhanh chóng xử lý các vấn đề bằng cách thu thập dữ liệu về lỗi, nhóm các sự cố
//tương tự,phân loại mức độ nghiêm trọng và cung cấp thông tin chi tiết về lỗi
//class AdsInitializer : Initializer<String> {
//    private var isCalled =false
//    override fun create(context: Context): String {
//        CoroutineScope(Dispatchers.IO).launch {
//            if (!isCalled){
//                try {
//                    //Khởi chạy SDK Quảng cáo của Google
//                    MobileAds.initialize(
//                        context,
//                        initializationConfig = TODO(),
//                        listener = TODO()
//                    )
//                }catch (e:Exception){
//                   FirebaseCrashlytics.getInstance().recordException(e)
//                }
//                isCalled=true
//            }
//        }
//        return ""
//    }
//
//    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
//        return mutableListOf()
//    }
//}