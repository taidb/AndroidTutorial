package com.example.androidtutorial.activity.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.R
import com.example.androidtutorial.activity.model.Student
import com.example.androidtutorial.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val tag = "LifecycleDemo2"
    private val extra = "EXTRA_DATA"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri=intent.data
        binding.imageView.setImageURI(imageUri)

        binding.btnClick.setOnClickListener {
            val data = Intent()
            data.putExtra(extra,"Quay lại trang 1")
            setResult(RESULT_OK,data)
            finish()
        }

        // Kiểm tra và khôi phục trạng thái ở đây
        if (savedInstanceState != null) {
            val savedValue = savedInstanceState.getString("my_state")
            Log.d(tag, "onCreate(): Restored state: $savedValue")
        } else {
            Log.d(tag, "onCreate(): No saved state found.")
        }

        //nhận dữ liệu :kiểu string,int, array tương tự:
        when {
            intent.hasExtra("data") -> {
                val data = intent.getStringExtra("data")
                binding.txtReceiveData.text = data
            }
            intent.hasExtra("data1") -> {
                val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra("data1", Student::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<Student>("data1")
                }

                val formattedText = getString(R.string.student_info, student?.name, student?.age, student?.address)
                binding.txtReceiveData.text = formattedText

            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "============ onStart() ============")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "============ onResume() ============")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "============ onPause() ============")
        // Lưu trạng thái UI nhẹ ở đây nếu cần
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "============ onStop() ============")
        // Giải phóng tài nguyên khi Activity không hiển thị
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "============ onDestroy() ============")
        // Giải phóng tất cả tài nguyên còn lại
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "============ onRestart() ============")
    }

    // Lưu trạng thái trước khi Activity có thể bị hủy bởi hệ thống
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(tag, "============ onSaveInstanceState() ============")
        // Ví dụ lưu một giá trị
        outState.putString("my_state", "Hello from saved state!")
        super.onSaveInstanceState(outState) // Luôn gọi superclass cuối cùng
    }
}