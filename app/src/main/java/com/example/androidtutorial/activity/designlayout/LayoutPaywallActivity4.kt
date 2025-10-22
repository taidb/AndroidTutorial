package com.example.androidtutorial.activity.designlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityLayoutPaywall4Binding

class LayoutPaywallActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutPaywall4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywall4Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnTryFree.setOnClickListener {
            handleButtonLoading(button = binding.btnTryFree, progress = binding.progress)
        }

        binding.btnContinue.setOnClickListener {
            handleButtonLoading(button = binding.btnContinue, progress = binding.progress)
        }
    }

    private fun handleButtonLoading(button: AppCompatTextView, progress: View) {
        button.text = ""
        button.isEnabled = false
        binding.txtAutoRenew.visibility = View.INVISIBLE
        button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
        progress.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            progress.visibility = View.GONE
            button.isEnabled = true
            button.text = getString(
                if(button.id==R.id.btnTryFree){
                    R.string.try_for_free
                }else{
                    R.string.continue1
                }

            )
            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dark)
        }, 2000)

    }
}
