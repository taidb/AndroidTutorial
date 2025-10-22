package com.example.androidtutorial.activity.designlayout

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtutorial.R
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
            startActivity(Intent(this, LayoutPaywallActivity1::class.java))
        }

        binding.btnPage3.setOnClickListener {
            startActivity(Intent(this, LayoutPaywallActivity2::class.java))
        }

        binding.btnPage4.setOnClickListener {
            startActivity(Intent(this, LayoutPaywallActivity3::class.java))
        }

        binding.btnPage5.setOnClickListener {
            startActivity(Intent(this, LayoutPaywallActivity4::class.java))
        }

        binding.btnPage6.setOnClickListener {
            startActivity(Intent(this, DialogBottomSheetActivity::class.java))
        }

        binding.btnPage7.setOnClickListener {
            startActivity(Intent(this, DialogBottomSheetActivity2::class.java))
        }
    }
}

