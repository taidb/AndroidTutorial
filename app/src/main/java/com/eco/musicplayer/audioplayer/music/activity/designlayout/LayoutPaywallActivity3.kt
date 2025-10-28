package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutPaywall3Binding

class LayoutPaywallActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywall3Binding
    private var selected = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        selected = 1
        binding.idClose.setOnClickListener {
            finish()
        }
        setupPlanSelection()
        setupSwitchListener()
        setupButtons()
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
