package com.example.androidtutorial.activity.designlayout

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.activity.util.SpannableHelper
import com.example.androidtutorial.databinding.ActivityLayoutPaywallBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LayoutPaywallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywallBinding
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        setupBottomSheet()
        setupSpannableText()
        setupClickListeners()
    }

    // --- 1. Khởi tạo UI ---
    private fun initUI() {
        binding.txtPriceOff.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtTryAgain.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    // --- 2. Cấu hình BottomSheet ---
    private fun setupBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.isFitToContents = true
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.35).toInt()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    // --- 3. Cấu hình text có link điều khoản và bảo mật ---
    private fun setupSpannableText() {
        SpannableHelper.setupTermsAndPrivacyText(
            this,
            binding.txtPrivacyPolicies,
            onTermsClick = {
                println("TERMS CLICKED IN ACTIVITY")
                Toast.makeText(this, "Điều khoản dịch vụ", Toast.LENGTH_SHORT).show()
            },
            onPrivacyClick = {
                println("PRIVACY CLICKED IN ACTIVITY")
                Toast.makeText(this, "Chính sách bảo mật", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // --- 4. Thiết lập các sự kiện click ---
    private fun setupClickListeners() {
        binding.idClose.setOnClickListener { finish() }

        binding.btnClaim.setOnClickListener {
            handleClaimClick()
        }
    }

    // --- 5. Xử lý khi nhấn nút Claim ---
    private fun handleClaimClick() {
        with(binding) {
            btnClaim.text = ""
            btnClaim.isEnabled = false
            progress.visibility = View.VISIBLE
            linearLayout.visibility = View.INVISIBLE
            linearLayout1.visibility = View.INVISIBLE
            view.visibility = View.VISIBLE
            txtDescription.visibility = View.INVISIBLE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            with(binding) {
                view.visibility = View.INVISIBLE
                progress.visibility = View.GONE
                btnClaim.visibility = View.GONE
                linearLayout2.visibility = View.VISIBLE
            }
        }, 3000)
    }
}
