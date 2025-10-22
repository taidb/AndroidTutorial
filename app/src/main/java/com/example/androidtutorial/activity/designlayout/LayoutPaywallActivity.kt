    package com.example.androidtutorial.activity.designlayout

    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.view.View
    import android.view.WindowManager
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.example.androidtutorial.activity.util.SpannableHelper
    import com.example.androidtutorial.databinding.ActivityLayoutPaywallBinding
    import com.google.android.material.bottomsheet.BottomSheetBehavior

    class LayoutPaywallActivity : AppCompatActivity() {
        private lateinit var binding: ActivityLayoutPaywallBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLayoutPaywallBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.txtPriceOff.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            binding.txtTryAgain.paintFlags=android.graphics.Paint.UNDERLINE_TEXT_FLAG

            val bottomSheet = binding.bottomSheet
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.isFitToContents = true
            behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.35).toInt()


            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

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

            binding.btnClaim.setOnClickListener {
                binding.btnClaim.text = ""
                binding.btnClaim.isEnabled = false

                binding.progress.visibility = View.VISIBLE

                binding.linearLayout.visibility = View.INVISIBLE
                binding.linearLayout1.visibility = View.INVISIBLE
                binding.view.visibility = View.VISIBLE
                binding.txtDescription.visibility = View.INVISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.view.visibility = View.INVISIBLE
                    binding.progress.visibility = View.GONE
                    binding.btnClaim.visibility = View.GONE
                    binding.linearLayout2.visibility = View.VISIBLE
                }, 3000)
            }


        }
    }