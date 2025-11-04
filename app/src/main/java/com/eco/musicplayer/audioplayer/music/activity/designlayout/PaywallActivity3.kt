package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutPaywall3Binding

class PaywallActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywall3Binding
    private var selected = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        selected = 1
        binding.icClose.setOnClickListener {
            finish()
        }
        setupWindow()
        setupPlanSelection()
        setupSwitchListener()
        setupButtons()
        binding.txtFreeTrial.setOnClickListener {
            binding.customSwitch.isChecked = !binding.customSwitch.isChecked
        }
        binding.txtEnable.setOnClickListener {
            binding.customSwitch.isChecked = !binding.customSwitch.isChecked
        }

    }
    @SuppressLint("UseKtx")
    private fun setupWindow() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#80000000")))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            // Thiết lập status bar
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@PaywallActivity3, R.color.color_F0F7FF)
            navigationBarColor = Color.TRANSPARENT

            // Sử dụng WindowInsetsController cho API 30+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setDecorFitsSystemWindows(false)
                val controller = decorView.windowInsetsController
                controller?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            // Cho các API cũ hơn
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        )
            } else {
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        )
            }
        }
    }
    private fun setupPlanSelection() {
        with(binding) {
            idYearly.setOnClickListener {
                selected = 1
                selectTextView(idYearly, idWeekly)
                updateAutoRenewText()
            }
            idWeekly.setOnClickListener {
                selected = 2
                selectTextView(idWeekly, idYearly)
                updateAutoRenewText()
            }
        }
    }

    private fun selectTextView(selectedView: AppCompatTextView, unselectedView: AppCompatTextView) {
        selectedView.setBackgroundResource(R.drawable.rounded_corners_6)
        selectedView.setTextColor(ContextCompat.getColor(this, R.color.dark))
        unselectedView.background = null
        unselectedView.setTextColor(ContextCompat.getColor(this, R.color.white_EAF5F8))

        selectedView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(120).start()
        unselectedView.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
    }


    private fun setupSwitchListener() {
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleSwitchViews(isChecked)
        }
    }

    private fun toggleSwitchViews(isChecked: Boolean) = with(binding) {
        if (isChecked) {
            txtFreeTrial.visibility = View.VISIBLE
            frameLayout.visibility = View.VISIBLE
            frameLayout2.visibility = View.GONE
            txtEnable.visibility = View.GONE
        } else {
            txtFreeTrial.visibility = View.GONE
            frameLayout.visibility = View.GONE
            frameLayout2.visibility = View.VISIBLE
            txtEnable.visibility = View.VISIBLE
        }
        updateAutoRenewText()
    }


    private fun updateAutoRenewText() = with(binding) {
        val textRes = if (binding.customSwitch.isChecked) {
            if (selected == 1) R.string.after_free_trial_ends_yearly
            else R.string.after_free_trial_ends_weekly
        } else {
            if (selected == 1) R.string.year_enable
            else R.string.weekly_enable
        }
        txtAutoRenew.text = getString(textRes)
        txtAutoRenew.visibility = View.VISIBLE
    }

    private fun setupButtons() = with(binding) {
        btnTryForFree.setOnClickListener { handleButtonLoading(btnTryForFree, progress) }
        btnContinue.setOnClickListener { handleButtonLoading(btnContinue, progress2) }
    }

    private fun handleButtonLoading(button: AppCompatTextView, progress: View) {
        button.text = ""
        button.isEnabled = false
        binding.txtAutoRenew.visibility = View.GONE

        button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
        progress.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            progress.visibility = View.GONE
            button.isEnabled = true
            button.text = getString(
                if (button.id == R.id.btnTryForFree)
                    R.string.try_for_free
                else
                    R.string.continue1
            )
            button.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.dark)

            binding.txtAutoRenew.visibility = View.VISIBLE
        }, 3000)
    }
}
