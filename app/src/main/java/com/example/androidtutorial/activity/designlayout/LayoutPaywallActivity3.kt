package com.example.androidtutorial.activity.designlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityLayoutPaywall3Binding

class LayoutPaywallActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutPaywall3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLayoutPaywall3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPlanSelection()
        setupSwitchListener()
        setupButtons()
    }


    private fun setupPlanSelection() {
        binding.idYearly.setOnClickListener {
            selectTextView(binding.idYearly, binding.idWeekly)
        }
        binding.idWeekly.setOnClickListener {
            selectTextView(binding.idWeekly, binding.idYearly)
        }
    }

    private fun selectTextView(selected: AppCompatTextView, unselected: AppCompatTextView) {
        selected.setBackgroundResource(R.drawable.rounded_corners_6)
        selected.setTextColor(ContextCompat.getColor(this, R.color.dark))
        unselected.background = null
        unselected.setTextColor(ContextCompat.getColor(this, R.color.white_EAF5F8))

        selected.animate().scaleX(1.05f).scaleY(1.05f).setDuration(120).start()
        unselected.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
    }


    private fun setupSwitchListener() {
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleSwitchViews(isChecked)
        }
    }

    private fun toggleSwitchViews(isChecked: Boolean) {
        if (isChecked) {
            binding.txtFreeTrial.visibility = View.VISIBLE
            binding.frameLayout.visibility = View.VISIBLE
            binding.frameLayout2.visibility = View.GONE
            binding.txtEnable.visibility = View.GONE
        } else {
            binding.txtFreeTrial.visibility = View.GONE
            binding.frameLayout.visibility = View.GONE
            binding.frameLayout2.visibility = View.VISIBLE
            binding.txtEnable.visibility = View.VISIBLE
        }
    }


    private fun setupButtons() {
        binding.btnTryForFree.setOnClickListener {
            handleButtonLoading(binding.btnTryForFree, binding.progress)
        }

        binding.btnContinue.setOnClickListener {
            handleButtonLoading(binding.btnContinue, binding.progress2)
        }
    }


    private fun handleButtonLoading(button: AppCompatTextView, progress: View) {
        button.text = ""
        button.isEnabled = false
        binding.txtAutoRenew.visibility = View.GONE

        // Màu nền xám khi đang loading
        button.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
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
        }, 3000)
    }
}
