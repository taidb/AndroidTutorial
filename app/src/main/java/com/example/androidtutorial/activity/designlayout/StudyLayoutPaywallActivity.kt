package com.example.androidtutorial.activity.designlayout

import PaywallBottomSheetDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.androidtutorial.databinding.ActivityStudyLayoutPaywallBinding



class StudyLayoutPaywallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyLayoutPaywallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudyLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnPage1.setOnClickListener {
            startActivity(Intent(this, LayoutPaywallActivity::class.java))
        }

        binding.btnPage2.setOnClickListener {
            val paywallDialog = LayoutPaywallActivity1(this)
            paywallDialog.show()

         //   startActivity(Intent(this, LayoutPaywallActivity1::class.java))
        }

        binding.btnPage3.setOnClickListener {
            val paywallDialog = LayoutPaywallActivity2(this)
            paywallDialog.show()
           // startActivity(Intent(this, LayoutPaywallActivity2::class.java))
        }

        binding.btnPage4.setOnClickListener {
            startActivity(Intent(this, LayoutPaywallActivity3::class.java))
        }

        binding.btnPage5.setOnClickListener {
            startActivity(Intent(this, LayoutPaywallActivity4::class.java))
        }

        // Trong Activity/Fragment của bạn
        binding.btnPage6.setOnClickListener {
            val bottomSheet = PaywallBottomSheetDialog()
            bottomSheet.show(supportFragmentManager, "PaywallBottomSheet")
        }

        binding.btnPage7.setOnClickListener {
            startActivity(Intent(this, DialogBottomSheetActivity2::class.java))
        }
    }
}

