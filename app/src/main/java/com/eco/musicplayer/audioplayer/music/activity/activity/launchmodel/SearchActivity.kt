package com.eco.musicplayer.audioplayer.music.activity.activity.launchmodel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        //ví dụ sự dụng singleTask
        val homeIntent = Intent(this, com.eco.musicplayer.audioplayer.music.activity.activity.launchmodel.HomeActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(homeIntent) // Xóa hết stack và hiển thị HomeActivity
    }

    //nếu Activity đã ở top của stack thì không tạo mới gọi onNewIntent() -> tránh tạo nhiêều instance -ví dụ khi click và notification không cần tạo mới nếu đang hiện thị
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newQuery = intent.getStringExtra("query")
        if (newQuery != null) {
            // Xử lý truy vấn mới ở đây
            println()
        }else{
            //
        }
    }
}