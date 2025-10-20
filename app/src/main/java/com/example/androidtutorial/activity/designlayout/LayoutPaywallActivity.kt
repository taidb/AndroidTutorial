package com.example.androidtutorial.activity.designlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.databinding.ActivityLayoutPaywallBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LayoutPaywallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutPaywallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gạch ngang giá cũ
        binding.txtPriceOff.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG

        // Lấy BottomSheet từ DataBinding (ví dụ bạn gán id="bottomSheet" cho view bên dưới)
        val bottomSheet = binding.bottomSheet

        // Gán behavior
        val behavior = BottomSheetBehavior.from(bottomSheet)

        // Giữ tỉ lệ tương ứng 360:325 khi ở trạng thái ban đầu
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.35).toInt()

        // Cho phép mở full màn hình khi kéo lên
        behavior.isFitToContents = false
        behavior.expandedOffset = 0
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
