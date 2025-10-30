package com.eco.musicplayer.audioplayer.music.activity.remoteconfig


import android.util.Log
import com.eco.musicplayer.audioplayer.music.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.google.gson.Gson

class RemoteConfig {

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig.apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .setFetchTimeoutInSeconds(0)
                .build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
            reset()
        }
    }

    private var onComplete: (() -> Unit)? = null

    fun fetchAndActivate(complete: (() -> Unit)? = null) {
        this.onComplete = complete
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) onComplete?.invoke()
        }
    }

    fun registerRealtimeUpdate() {
        runCatching {
            remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                   // ECOLog.showLog("onUpdate")

                    Log.d("ECOLog", "onUpdate")

                }

                override fun onError(error: FirebaseRemoteConfigException) {

                }
            })
        }.getOrElse {
        }
    }

    fun getPaywallConfig():PaywallConfig?{
        return  try {
            val configJson=remoteConfig.getString("pricing_config")
            if (configJson.isNotEmpty()){
                Gson().fromJson(configJson,PaywallConfig::class.java)
            }else{
                null
            }
        }catch (e:Exception){
            Log.e("RemoteConfig","Error ${e.message}")
            null
        }
    }

    fun destroy() {
        onComplete = null
    }
}