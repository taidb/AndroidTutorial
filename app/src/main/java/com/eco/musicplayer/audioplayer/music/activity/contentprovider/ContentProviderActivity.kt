package com.eco.musicplayer.audioplayer.music.activity.contentprovider

import android.content.ContentValues
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityContentProviderBinding
class ContentProviderActivity : AppCompatActivity() {
    private val binding by lazy { ActivityContentProviderBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val values = ContentValues()
            values.put("name", "User ${(0..100).random()}")

            contentResolver.insert(UserProvider.USERS_URI, values)
        }

        binding.btnLoad.setOnClickListener {
            loadUsers()
        }
    }

    private fun loadUsers() {
        val cursor = contentResolver.query(
            UserProvider.USERS_URI,
            null,
            null,
            null,
            null
        )

        val sb = StringBuilder()

        while (cursor?.moveToNext() == true) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            sb.append("ID: $id - Name: $name\n")
        }

        cursor?.close()

        binding.tvResult.text = sb.toString()
    }
}

