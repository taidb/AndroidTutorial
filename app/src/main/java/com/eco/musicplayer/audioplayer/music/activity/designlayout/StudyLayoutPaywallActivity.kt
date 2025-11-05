package com.eco.musicplayer.audioplayer.music.activity.designlayout

import PaywallBottomSheetDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eco.musicplayer.audioplayer.music.activity.network.isNetworkAvailable
import com.eco.musicplayer.audioplayer.music.activity.network.isWifeEnabled
import com.eco.musicplayer.audioplayer.music.activity.util.setSafeOnClickListener
import com.eco.musicplayer.audioplayer.music.databinding.ActivityStudyLayoutPaywallBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Có 3 cách để xử lí người dùng click nhanh hoặc nhiều lần vào 1 view :
//C1:  Sự dụng biến cờ để theo dõi trạng thái xử lí:
// - Tạo 1 biến isProcessing khi người dùng bấm vào kiểm tra isProcessing.
// Nếu true, bỏ qua sự kiện click.Nếu fasle set isProcessing thành true và thực hiện xử lí
// Nếu xử lí xong thì chuyêển về thành false
//C2:Sử dụng setClickable() và postDelayed :
//Khi người dùng click, đặt myView.setClickable(false).
//Thực hiện xử lý.
//Sử dụng myView.postDelayed để gọi lại myView.setClickable(true) sau một khoảng thời gian nhất định.
//C3 : khi đang chờ xử lí có thể nút đó và hiện thị 1 widget khác ( ví dụ như là ProgreessBar)
// để thông báo cho người dùng biết ưứng dụng đang  được xử lí
//C4: Sử dụng thư viện "debouncing"

class StudyLayoutPaywallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyLayoutPaywallBinding
    private var isProcessing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudyLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnPage1.setSafeOnClickListener {
            startActivity(Intent(this, PaywallActivity::class.java))
        }

        binding.btnPage2.setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    it.isEnabled = true
                }
            }
            val paywallDialog = PaywallActivity1(this)
            paywallDialog.show()

            //   startActivity(Intent(this, LayoutPaywallActivity1::class.java))
        }

        binding.btnPage3.setOnClickListener {
            if (isProcessing) {
                return@setOnClickListener
            }
            isProcessing = true
            val paywallDialog = PaywallActivity2(this)
            paywallDialog.show()
            it.postDelayed({
                isProcessing = false
            }, 1000)
            // startActivity(Intent(this, LayoutPaywallActivity2::class.java))
        }

        binding.btnPage4.setOnClickListener {
            it.isEnabled = false
            runCatching { it.postDelayed({ it.isEnabled = true }, 1000) }
            startActivity(Intent(this, PaywallActivity3::class.java))
        }

        binding.btnPage5.setOnClickListener {
            startActivity(Intent(this, PaywallActivity4::class.java))
        }


        binding.apply {
            btnPage6.setOnClickListener {
                it.isEnabled = false
                progress.visibility = View.VISIBLE
                val bottomSheet = PaywallBottomSheetDialog()
                bottomSheet.show(supportFragmentManager, "PaywallBottomSheet")
                progress.visibility = View.INVISIBLE

                bottomSheet.setOnDismissListener {
                    progress.visibility = View.INVISIBLE
                    it.isEnabled = true
                }
            }
        }

        binding.btnPage7.setOnClickListener {
            startActivity(Intent(this, DialogBottomSheet2::class.java))
        }
    }


}

