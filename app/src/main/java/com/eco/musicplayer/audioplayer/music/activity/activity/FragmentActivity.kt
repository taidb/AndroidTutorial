package com.eco.musicplayer.audioplayer.music.activity.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.fragment.Fragment1
import com.eco.musicplayer.audioplayer.music.activity.fragment.Fragment2
import com.eco.musicplayer.audioplayer.music.activity.viewmodel.ItemViewModel

class FragmentActivity : AppCompatActivity() {
    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fragment)

        setupFragments()
        setupObservers()
    }

    private fun setupFragments() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        // Thêm Fragment1
        transaction.replace(
            R.id.ConnectFragment,
            Fragment1(),
            "fragment1"
        )

        // Thêm Fragment2
        transaction.replace(
            R.id.ConnectFragment2,
            Fragment2(),
            "fragment2"
        )

        transaction.commit()
    }

    private fun setupObservers() {
        // Activity cũng có thể observe dữ liệu từ ViewModel
        viewModel.selectedItem.observe(this) { item ->
            Toast.makeText(
                this,
                "Activity đã nhận được: ${item.name} - ID: ${item.id}",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.filteredList.observe(this) { items ->
            // Activity có thể xử lý dữ liệu nếu cần
            supportActionBar?.subtitle = "Có ${items.size} items"
        }
    }
}