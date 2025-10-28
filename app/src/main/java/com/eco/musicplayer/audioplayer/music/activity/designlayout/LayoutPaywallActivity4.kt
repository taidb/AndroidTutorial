package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutPaywall4Binding

class LayoutPaywallActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywall4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.btnTryFree.setOnClickListener {
            startButtonLoading(binding.btnTryFree)
        }

        binding.btnContinue.setOnClickListener {
            startButtonLoading(binding.btnContinue)
        }
    }

    private fun startButtonLoading(button: AppCompatTextView) {
        disableButton(button)
        showLoading()
        delayResetButton(button)
    }

    private fun disableButton(button: AppCompatTextView) {
        button.text = ""
        button.isEnabled = false
        binding.txtAutoRenew.visibility = View.INVISIBLE
        button.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun delayResetButton(button: AppCompatTextView) {
        Handler(Looper.getMainLooper()).postDelayed({
            resetButtonState(button)
        }, 2000)
    }

    private fun resetButtonState(button: AppCompatTextView) {
        binding.progress.visibility = View.GONE
        button.isEnabled = true
        button.text = getButtonText(button)
        button.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.dark)
    }

    private fun getButtonText(button: AppCompatTextView): String {
        return getString(
            if (button.id == R.id.btnTryFree) {
                R.string.try_for_free
            } else {
                R.string.continue1
            }
        )
    }
}
