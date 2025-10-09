package com.example.androidtutorial.activity.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.activity.Student
import com.example.androidtutorial.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val tag = "LifecycleDemo2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnClick.setOnClickListener {
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
        val intent1 = intent
        val data = intent1.getStringExtra("data")
        binding.txtReceiveData.text=data

        //nhận dữ liệu kiểu đối tượng
        val intent2 = intent
        val student = intent2.getSerializableExtra("data1") as Student
        binding.txtReceiveData.text = "${student.name} - ${student.age} - ${student.address}"
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