package com.eco.musicplayer.audioplayer.music.activity.adsme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.activity.AdDemoApp
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
        private const val SPLASH_DELAY = 3000L // Tăng lên 3 giây
    }

    private var isAdShown = false
    private var isAdLoaded = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d(TAG, "=== SPLASH ACTIVITY STARTED ===")

        val app = application as? AdDemoApp
        if (app == null) {
            Log.e(TAG, "ERROR: Application is not AdDemoApp!")
            proceedToMainActivity()
            return
        }

        // LOAD APP OPEN AD VỚI CALLBACK
        Log.d(TAG, "Loading App Open Ad...")
        app.adManager.loadAppOpenAd(this,
            onAdLoaded = {
                Log.d(TAG, "App Open Ad LOADED - Attempting to show...")
                isAdLoaded = true
                showAppOpenAd()
            }
        )

        // Timeout nếu quảng cáo không load được
        handler.postDelayed({
            Log.d(TAG, "AD LOAD TIMEOUT - isAdLoaded: $isAdLoaded, isAdShown: $isAdShown")
            if (!isAdShown) {
                proceedToMainActivity()
            }
        }, SPLASH_DELAY)
    }

    private fun showAppOpenAd() {
        Log.d(TAG, "showAppOpenAd() called")

        if (isFinishing || isAdShown) {
            Log.d(TAG, "Cannot show ad - finishing: $isFinishing, already shown: $isAdShown")
            return
        }

        val app = application as? AdDemoApp ?: return
        val success = app.adManager.showAppOpenAd(this)

        Log.d(TAG, "showAppOpenAd() result: $success")

        if (success) {
            isAdShown = true
            Log.d(TAG, "App Open Ad SHOULD BE VISIBLE NOW")
        } else {
            Log.d(TAG, "Failed to show App Open Ad - proceeding to main")
            proceedToMainActivity()
        }
    }

    fun proceedToMainActivity() {
        if (isFinishing) return

        Log.d(TAG, "Proceeding to AdMobActivity...")
        startActivity(Intent(this, AdMobActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "SplashActivity destroyed")
    }
}