package com.example.androidtutorial.activity.designlayout

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityDialogBottomSheet2Binding

class DialogBottomSheetActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityDialogBottomSheet2Binding
    private var selectedPlan = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBottomSheet2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInitialLayout()
        setupListeners()
    }


    private fun setupInitialLayout() {
        showTryForFreeLayout()
    }

    private fun setupListeners() {
        binding.idClose.setOnClickListener { finish() }

        binding.btnIap1.setOnClickListener { selectPlan(1) }
        binding.btnIap2.setOnClickListener { selectPlan(2) }

        binding.btnTryForFree.setOnClickListener {
            handleTryForFreeClick()
        }
    }


    private fun selectPlan(plan: Int) {
        selectedPlan = plan
        when (plan) {
            1 -> {
                binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
                binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
                binding.tvMostPopular.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.color_8147FF)
                binding.txtAutoRenew.text =
                    getString(R.string.after_free_trial_ends_yearly_max)
            }

            2 -> {
                binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_no_pw_4_unselected)
                binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
                binding.tvMostPopular.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.color_908DAC)
                binding.txtAutoRenew.text =
                    getString(R.string.after_free_trial_ends_weekly)
            }
        }
    }

    private fun handleTryForFreeClick() {
        if (selectedPlan == 1) {
            showLoadingState(true)
            handleButtonLoading(binding.btnTryForFree, binding.progress2, selectedPlan)
        } else {
            handleButtonLoading(binding.btnTryForFree, binding.progress2, selectedPlan)
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.linearLayout.visibility = View.INVISIBLE
            binding.linearLayout1.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE
            binding.progress.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_0F1E47C))
            binding.progress1.visibility = View.VISIBLE
            binding.progress1.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_0F1E47C))
        } else {
            binding.linearLayout.visibility = View.VISIBLE
            binding.linearLayout1.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
            binding.progress1.visibility = View.GONE
        }
    }


    private fun handleButtonLoading(
        button: AppCompatTextView,
        progress: View,
        plan: Int = -1
    ) {
        button.apply {
            text = ""
            isEnabled = false
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.color_5F5F5F)
        }

        setInfoTextsVisibility(isVisible = false)
        progress.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            progress.visibility = View.GONE

            if (plan == 1) showLoadingState(false)

            button.apply {
                isEnabled = true
                text = getString(
                    if (id == R.id.btnTryForFree) R.string.try_for_free else R.string.continue1
                )
                backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.color_8147FF)
            }

            setInfoTextsVisibility(isVisible = true)
        }, 2000)
    }

    private fun setInfoTextsVisibility(isVisible: Boolean) {
        binding.txtAutoRenew.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.txtNoPayment.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.txtCancel.visibility = if (isVisible) View.GONE else View.INVISIBLE
    }


    private fun showTryForFreeLayout() {
        binding.frameLayout.visibility = View.VISIBLE
        binding.frameLayout2.visibility = View.GONE
        binding.txtNoPayment.visibility = View.VISIBLE
        binding.txtCancel.visibility = View.GONE
    }

    private fun showContinueLayout() {
        binding.frameLayout.visibility = View.GONE
        binding.frameLayout2.visibility = View.VISIBLE
        binding.txtNoPayment.visibility = View.GONE
        binding.txtCancel.visibility = View.VISIBLE
    }
}
