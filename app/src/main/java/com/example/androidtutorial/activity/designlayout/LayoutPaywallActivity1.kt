package com.example.androidtutorial.activity.designlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.activity.util.SpannableHelper
import com.example.androidtutorial.databinding.ActivityLayoutPaywall1Binding
class LayoutPaywallActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywall1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall1Binding.inflate(layoutInflater)
        setContentView(binding.root)


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

            binding.progress.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progress.visibility = View.GONE
                binding.btnClaimOffer.visibility = View.GONE
                binding.linearLayout2.visibility = View.VISIBLE
            }, 3000)
        }


    }
}