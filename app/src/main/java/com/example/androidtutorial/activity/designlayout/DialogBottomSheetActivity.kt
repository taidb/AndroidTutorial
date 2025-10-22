package com.example.androidtutorial.activity.designlayout

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityDialogBottomSheetBinding

class DialogBottomSheetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDialogBottomSheetBinding
    private var selectedPlan=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showTryForFreeLayout()
        binding.btnIap1.setOnClickListener {
            selectedPlan = 1
            binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
            binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
            binding.tvMostPopular.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.color_8147FF)
        }
        binding.btnIap2.setOnClickListener {
            selectedPlan = 2
            binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_no_pw_4_unselected)
            binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
            binding.tvMostPopular.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.color_908DAC)

        }

        binding.btnTryForFree.setOnClickListener {
            if (selectedPlan == 1) {
                // Ẩn layout và hiển thị progress
                binding.linearLayout.visibility = View.INVISIBLE
                binding.linearLayout1.visibility = View.INVISIBLE
                binding.progress.visibility = View.VISIBLE
                binding.progress.indeterminateTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.color_0F1E47C)
                )
                binding.progress1.visibility = View.VISIBLE
                binding.progress1.indeterminateTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.color_0F1E47C)
                )

                handleButtonLoading(
                    binding.btnTryForFree,
                    binding.progress2,
                    selectedPlan // Thêm tham số để biết plan nào đang được chọn
                )
            } else {
                handleButtonLoading(binding.btnTryForFree, binding.progress2, selectedPlan)
            }
        }
    }

        private fun handleButtonLoading(
            button: AppCompatTextView,
            progress: View,
            plan: Int = -1
        ) {
            button.text = ""
            button.isEnabled = false
            binding.txtAutoRenew.visibility = View.INVISIBLE
            binding.txtNoPayment.visibility = View.INVISIBLE
            binding.txtCancel.visibility = View.INVISIBLE

            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_5F5F5F)
            progress.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progress.visibility = View.GONE

                if (plan == 1) {
                    binding.linearLayout.visibility = View.VISIBLE
                    binding.linearLayout1.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.progress1.visibility = View.GONE
                }

                button.isEnabled = true
                button.text = getString(
                    if (button.id == R.id.btnTryForFree) {
                        R.string.try_for_free
                    } else {
                        R.string.continue1
                    }
                )
                button.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.color_8147FF)

                binding.txtAutoRenew.visibility = View.VISIBLE
                binding.txtNoPayment.visibility = View.VISIBLE
                binding.txtCancel.visibility = View.GONE
            }, 2000)
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
