package com.example.androidtutorial.activity.designlayout

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityLayoutPaywall3Binding

class LayoutPaywallActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutPaywall3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLayoutPaywall3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.idYearly.setOnClickListener {
            selectTextView(binding.idYearly, binding.idWeekly)

        }
        binding.idWeekly.setOnClickListener {
            selectTextView(binding.idWeekly, binding.idYearly)
        }
//        binding.customSwitch.apply {
//            thumbDrawable.setTintList(null)
//            trackDrawable.setTintList(null)
//
//        }

        binding.customSwitch.setOnCheckedChangeListener{_, isCheck ->
            if (isCheck){
                binding.txtFreeTrial.visibility = View.VISIBLE
                binding.frameLayout.visibility = View.VISIBLE
                binding.frameLayout2.visibility = View.GONE
                binding.txtEnable.visibility = View.GONE
            }else{
                binding.txtFreeTrial.visibility = View.GONE
                binding.frameLayout.visibility = View.GONE
                binding.frameLayout2.visibility = View.VISIBLE
                binding.txtEnable.visibility = View.VISIBLE
            }

        }

        binding.btnTryForFree.setOnClickListener {
            binding.btnTryForFree.text = ""
            binding.btnTryForFree.isEnabled = false
            binding.txtAutoRenew.visibility = View.GONE

            // Đổi màu nền khi loading
            binding.btnTryForFree.setBackgroundColor(ContextCompat.getColor(this, R.color.color_5F5F5F))

            binding.progress.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progress.visibility = View.GONE
                binding.btnTryForFree.visibility = View.GONE
                // binding.btnTryForFree.setBackgroundResource(R.drawable.button_gradient_red)
            }, 3000)
        }

        binding.btnContinue.setOnClickListener {
            binding.btnContinue.text = ""
            binding.btnContinue.isEnabled = false
            binding.txtAutoRenew.visibility = View.GONE

            // Đổi màu nền khi loading
            binding.btnContinue.setBackgroundColor(ContextCompat.getColor(this, R.color.color_5F5F5F))

            binding.progress.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progress.visibility = View.GONE
                binding.btnContinue.visibility = View.GONE
                // binding.btnTryForFree.setBackgroundResource(R.drawable.button_gradient_red)
            }, 3000)
        }


        binding.btnTryForFree.setOnClickListener {
            handleButtonLoading(
                button = binding.btnTryForFree,
                progress = binding.progress
            )
        }

        binding.btnContinue.setOnClickListener {
            handleButtonLoading(
                button = binding.btnContinue,
                progress = binding.progress2
            )
        }
    }

    private fun handleButtonLoading(button: AppCompatTextView, progress: View) {
        button.text = ""
        button.isEnabled = false
        binding.txtAutoRenew.visibility = View.GONE

        // Đổi màu nền nút sang xám trong lúc loading
        button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
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
            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dark)
        }, 3000)
    }



    private fun selectTextView(selected: AppCompatTextView, unselected: AppCompatTextView) {
        selected.setBackgroundResource(R.drawable.rounded_corners_6)
        selected.setTextColor(ContextCompat.getColor(this, R.color.dark))
        unselected.background = null
        unselected.setTextColor(ContextCompat.getColor(this, R.color.white_EAF5F8))
        selected.animate().scaleX(1.05f).scaleY(1.05f).setDuration(120).start()
        unselected.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
    }

}