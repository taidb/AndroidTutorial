package com.eco.musicplayer.audioplayer.music.activity.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.eco.musicplayer.audioplayer.music.databinding.ActivityTestIntentBinding

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
            val data = receivedBundle.getString("data3")
            val data1 = receivedBundle.getInt("data4")
            val data2 = receivedBundle.getString("data5")
            Log.d("TestIntent", "data3: $data")
            Log.d("TestIntent", "data4: $data1")
            Log.d("TestIntent", "data5: $data2")

        }
    }
}