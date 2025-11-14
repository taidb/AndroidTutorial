package com.eco.musicplayer.audioplayer.music.activity.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.viewmodel.ProfileViewModel
import com.eco.musicplayer.audioplayer.music.databinding.ActivityProfileBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
    }

    private fun setUpViews() {
        val userId = intent.getStringExtra("userId") ?: return
        profileViewModel.loadUser(userId)
        setUpObservers()
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            profileViewModel.user.collect { user ->
                user?.let {
                    binding.tvInfoUser.text = user.name
                    binding.tvInfoUser1.text = user.age.toString()
                }
            }
        }
    }


}