package com.eco.musicplayer.audioplayer.music.activity.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.UserRepository
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.viewmodel.UserViewModel
import com.eco.musicplayer.audioplayer.music.activity.adater.UserAdapter
import com.eco.musicplayer.audioplayer.music.activity.model.User
import com.eco.musicplayer.audioplayer.music.databinding.ActivityUserBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private val userViewModel: UserViewModel by viewModel()
    private val userRepository: UserRepository by inject()
    private val userAdapter: UserAdapter by lazy {
        UserAdapter { user ->
            openProfile(user.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        setUpObservers()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnAddUser.setOnClickListener {
            addNewUser()
        }
        binding.btnReload.setOnClickListener {
            userViewModel.loadUser()
        }
    }

    private fun addNewUser() {
        val newUser = User(
            id = System.currentTimeMillis().toString(),
            name = "New User",
            age = 30
        )
        userViewModel.addUser(newUser)
    }

    private fun openProfile(userId: String) {
        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra("userId", userId)
        }
        startActivity(intent)
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            userViewModel.user.collect { userList ->
                userAdapter.differ.submitList(userList)
            }
        }
        lifecycleScope.launch {
            userViewModel.loading.collect { isLoading ->
                binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rcvUser.apply {
            layoutManager = LinearLayoutManager(this@UserActivity)
            adapter = userAdapter
        }
    }
}
