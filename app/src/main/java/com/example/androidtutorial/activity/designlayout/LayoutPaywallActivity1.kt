package com.example.androidtutorial.activity.designlayout

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.androidtutorial.activity.util.SpannableHelper
import com.example.androidtutorial.databinding.ActivityLayoutPaywall1Binding

class LayoutPaywallActivity1(
    context: Context,
    private val onCloseClick: (() -> Unit)? = null,
    private val onClaimOfferClick: (() -> Unit)? = null
) : Dialog(context) {

    private lateinit var binding: ActivityLayoutPaywall1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall1Binding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        setupWindow()
        initViews()
        setupClickListeners()
    }

    private fun setupWindow() {
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun initViews() {
        binding.txtTryAgain.paintFlags = android.graphics.Paint.UNDERLINE_TEXT_FLAG

        SpannableHelper.setupTermsAndPrivacyText(
            context,
            binding.txtPrivacyPolicies,
            onTermsClick = {
                println("TERMS CLICKED IN DIALOG")
                Toast.makeText(context, "Điều khoản dịch vụ", Toast.LENGTH_SHORT).show()
            },
            onPrivacyClick = {
                println("PRIVACY CLICKED IN DIALOG")
                Toast.makeText(context, "Chính sách bảo mật", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupClickListeners() {
        binding.idClose.setOnClickListener {
            onCloseClick?.invoke() ?: dismiss()
        }

        binding.btnClaimOffer.setOnClickListener {
            onClaimOfferClick?.invoke() ?: handleClaimOffer()
        }

        binding.txtTryAgain.setOnClickListener {
            resetUI()
        }
    }

    private fun handleClaimOffer() {
        binding.btnClaimOffer.text = ""
        binding.btnClaimOffer.isEnabled = false
        binding.progress.visibility = View.VISIBLE
        binding.txtFreeAnnouncement.visibility = View.INVISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            binding.progress.visibility = View.GONE
            binding.btnClaimOffer.visibility = View.INVISIBLE
            binding.linearLayout2.visibility = View.VISIBLE
        }, 3000)
    }

    private fun resetUI() {
        binding.btnClaimOffer.text = context.getString(com.example.androidtutorial.R.string.claim_offer)
        binding.btnClaimOffer.isEnabled = true
        binding.btnClaimOffer.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
        binding.linearLayout2.visibility = View.GONE
        binding.txtFreeAnnouncement.visibility = View.VISIBLE
    }

    fun resetDialog() {
        resetUI()
    }
}