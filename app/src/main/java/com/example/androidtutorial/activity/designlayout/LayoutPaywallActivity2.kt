package com.example.androidtutorial.activity.designlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.androidtutorial.activity.util.SpannableHelper
import com.example.androidtutorial.databinding.ActivityLayoutPaywall2Binding

class LayoutPaywallActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityLayoutPaywall2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLayoutPaywall2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtTryAgain.paintFlags=android.graphics.Paint.UNDERLINE_TEXT_FLAG
        SpannableHelper.setupTermsAndPrivacyText(
            this,
            binding.includeActivity.txtPrivacyPolicies,
            onTermsClick = {
                println("TERMS CLICKED IN ACTIVITY")
                Toast.makeText(this, "Điều khoản dịch vụ", Toast.LENGTH_SHORT).show()
            },
            onPrivacyClick = {
                println("PRIVACY CLICKED IN ACTIVITY")
                Toast.makeText(this, "Chính sách bảo mật", Toast.LENGTH_SHORT).show()
            }
        )

        binding.btnClaimOffer.setOnClickListener {
            binding.btnClaimOffer.text = ""
            binding.btnClaimOffer.isEnabled = false
            binding.frameLayout.visibility = View.GONE

            binding.progress.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progress.visibility = View.GONE
                binding.btnClaimOffer.visibility = View.GONE
                binding.linearLayout2.visibility = View.VISIBLE
            }, 3000)
        }

    }
}