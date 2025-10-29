package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityDialogBottomSheetBinding

class DialogBottomSheet : AppCompatActivity() {

    private var binding: ActivityDialogBottomSheetBinding? = null
    private var selectedPlan = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupInitialState()
        setupClickListeners()
    }

    private fun setupInitialState() {
        showTryForFreeLayout()
    }

    private fun setupClickListeners() {
        binding?.icClose?.setOnClickListener { finish() }

        binding?.btnIap1?.setOnClickListener { selectPlan(1) }
        binding?.btnIap2?.setOnClickListener { selectPlan(2) }

        binding?.btnTryForFree?.setOnClickListener {
            handleTryForFreeClick()
        }
    }

    private fun selectPlan(plan: Int) {
        selectedPlan = plan

        when (plan) {
            1 -> applyPlan1UI()
            2 -> applyPlan2UI()
        }
    }

    private fun applyPlan1UI() {
        binding?.apply {
            btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
            btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
            tvMostPopular.backgroundTintList =
                ContextCompat.getColorStateList(this@DialogBottomSheet, R.color.color_8147FF)
            txtAutoRenew.setText(getString(R.string.after_free_trial_ends_yearly_max))
        }
    }

    private fun applyPlan2UI() {
        binding?.apply {
            btnIap2.setBackgroundResource(R.drawable.bg_btn_no_pw_4_unselected)
            btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
            tvMostPopular.backgroundTintList =
                ContextCompat.getColorStateList(this@DialogBottomSheet, R.color.color_908DAC)
            txtAutoRenew.setText(getString(R.string.after_free_trial_ends_weekly))
        }
    }

    private fun handleTryForFreeClick() {
        if (selectedPlan == 1) {
            showMainProgress(true)
        }

        binding?.let {
            handleButtonLoading(it.btnTryForFree, it.progress2, selectedPlan)
        }
    }

    private fun handleButtonLoading(
        button: AppCompatTextView,
        progress: View,
        plan: Int
    ) {
        prepareButtonForLoading(button, progress)

        Handler(Looper.getMainLooper()).postDelayed({
            resetButtonAfterLoading(button, progress, plan)
        }, 2000)
    }

    private fun prepareButtonForLoading(button: AppCompatTextView, progress: View) {
        button.text = ""
        button.isEnabled = false
        button.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
        progress.visibility = View.VISIBLE

        setTextGroupVisibility(false)
    }

    private fun resetButtonAfterLoading(
        button: AppCompatTextView,
        progress: View,
        plan: Int
    ) {
        progress.visibility = View.GONE

        if (plan == 1) showMainProgress(false)

        button.apply {
            isEnabled = true
            text = getString(R.string.try_for_free)
            backgroundTintList =
                ContextCompat.getColorStateList(this@DialogBottomSheet, R.color.color_8147FF)
        }

        setTextGroupVisibility(true)
    }

    private fun showMainProgress(show: Boolean) {
        binding?.apply {
            val visibilityMain = if (show) View.INVISIBLE else View.VISIBLE
            val visibilityProgress = if (show) View.VISIBLE else View.GONE

            linearLayout.visibility = visibilityMain
            linearLayout1.visibility = visibilityMain

            progress.visibility = visibilityProgress
            progress1.visibility = visibilityProgress

            val color = ContextCompat.getColor(this@DialogBottomSheet, R.color.color_0F1E47C)
            progress.indeterminateTintList = ColorStateList.valueOf(color)
            progress1.indeterminateTintList = ColorStateList.valueOf(color)
        }
    }

    private fun setTextGroupVisibility(show: Boolean) {
        binding?.apply {
            txtAutoRenew.visibility = if (show) View.VISIBLE else View.INVISIBLE
            txtNoPayment.visibility = if (show) View.VISIBLE else View.INVISIBLE
            txtCancel.visibility = if (show) View.GONE else View.INVISIBLE
        }
    }

    private fun showTryForFreeLayout() {
        binding?.apply {
            frameLayout.visibility = View.VISIBLE
            frameLayout2.visibility = View.GONE
            txtNoPayment.visibility = View.VISIBLE
            txtCancel.visibility = View.GONE
        }
    }

    private fun showContinueLayout() {
        binding?.apply {
            frameLayout.visibility = View.GONE
            frameLayout2.visibility = View.VISIBLE
            txtNoPayment.visibility = View.GONE
            txtCancel.visibility = View.VISIBLE
        }
    }

    // Public method để reset
    fun resetToInitialState() {
        selectedPlan = 1
        showTryForFreeLayout()
        selectPlan(1)
        setTextGroupVisibility(true)
        showMainProgress(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}