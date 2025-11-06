package com.eco.musicplayer.audioplayer.music.activity.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityDemoFragmentBinding

class DemoFragment : AppCompatActivity() {
    private lateinit var binding: ActivityDemoFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDemoFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.shoppingfragment) as? NavHostFragment
        val navController = navHostFragment?.navController
        navController?.let { binding.bottomNavigation.setupWithNavController(it) }

    }
}