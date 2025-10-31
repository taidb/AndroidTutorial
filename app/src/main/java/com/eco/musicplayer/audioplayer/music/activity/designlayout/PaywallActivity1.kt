package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.eco.musicplayer.audioplayer.music.activity.util.SpannableHelper
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutPaywall1Binding


class PaywallActivity1(
    context: Context,
    private val onCloseClick: (() -> Unit)? = null,
    private val onClaimOfferClick: (() -> Unit)? = null
) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    private lateinit var binding: ActivityLayoutPaywall1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall1Binding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        setupWindow()
        initViews()
        setupClickListeners()
        // Đợi view được layout xong rồi mới set gradient
//        binding.root.post {
//            setGradientTextWithShadow(binding.txtPercent, "#F3F3FC", "#A2B1DA", 135f)
//            setGradientTextWithShadow(binding.txtOff, "#EEEEF2", "#C8D0E7", 135f)
//        }
    }

    @SuppressLint("UseKtx")
    private fun setupWindow() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#80000000")))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT

            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
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
        binding.icClose.setOnClickListener {
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
        binding.txtPercent.visibility=View.INVISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progress.visibility = View.GONE
            binding.btnClaimOffer.visibility = View.INVISIBLE
            binding.linearLayout2.visibility = View.VISIBLE
        }, 3000)
    }

    private fun resetUI() {
        binding.btnClaimOffer.text =
            context.getString(com.eco.musicplayer.audioplayer.music.R.string.claim_offer)
        binding.btnClaimOffer.isEnabled = true
        binding.btnClaimOffer.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
        binding.linearLayout2.visibility = View.GONE
        binding.txtFreeAnnouncement.visibility = View.VISIBLE
        binding.txtPercent.visibility=View.VISIBLE
    }

//    private fun setGradientTextWithShadow(
//        textView: TextView,
//        startColor: String = "#F3F3FC",
//        endColor: String = "#A2B1DA",
//        angle: Float = 135f
//    ) {
//        textView.post {
//            val width = textView.paint.measureText(textView.text.toString())
//            val height = textView.textSize
//
//            if (width == 0f || height == 0f) return@post
//
//            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//
//            val angleRad = Math.toRadians(angle.toDouble())
//            val length = Math.sqrt((width * width + height * height).toDouble()).toFloat()
//            val xEnd = (cos(angleRad) * length).toFloat()
//            val yEnd = (sin(angleRad) * length).toFloat()
//
//            val shader = LinearGradient(
//                0f, 0f, xEnd, yEnd,
//                intArrayOf(
//                    Color.parseColor(startColor),
//                    Color.parseColor(endColor)
//                ),
//                null,
//                Shader.TileMode.CLAMP
//            )
//            textView.paint.shader = shader
//            textView.invalidate()
//        }
//    }



}