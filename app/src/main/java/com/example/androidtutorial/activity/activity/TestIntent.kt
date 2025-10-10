package com.example.androidtutorial.activity.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityTestIntentBinding
import androidx.core.net.toUri
import kotlin.math.log

class TestIntent : AppCompatActivity() {
    private lateinit var binding: ActivityTestIntentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestIntentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnClick.setOnClickListener {
            val uri = "content://media/external/images/media/1".toUri()
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "image/*")
                `package` = "com.example.androidtutorial"
            }
            startActivity(intent)
        }

        //nhận bundle từ activity khác
        val receivedBundle: Bundle? = intent.extras
        if (receivedBundle != null) {
            val data = receivedBundle.getString("data")
            val data1 = receivedBundle.getInt("data1")
            val data2 = receivedBundle.getString("data2")
            Log.d("TestIntent", "data: $data")
            Log.d("TestIntent", "data1: $data1")
            Log.d("TestIntent", "data2: $data2")

        }
    }
}