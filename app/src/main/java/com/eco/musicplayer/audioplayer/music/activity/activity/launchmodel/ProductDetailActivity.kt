package com.eco.musicplayer.audioplayer.music.activity.activity.launchmodel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.model.Student

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        //ví dụ để suwj dụng singleTop
        val searchIntent = Intent(this, SearchActivity::class.java)
        searchIntent.putExtra("search_query", "android development")
        startActivity(searchIntent);
    }

    //mỗi lần click sẽ tạo ra instance mới
    fun onProductClick(student: Student){
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)
    }

}