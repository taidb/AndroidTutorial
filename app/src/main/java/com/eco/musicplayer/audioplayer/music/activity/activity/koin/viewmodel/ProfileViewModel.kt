package com.eco.musicplayer.audioplayer.music.activity.activity.koin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.UserRepository
import com.eco.musicplayer.audioplayer.music.activity.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository):ViewModel() {
    private val _user =MutableStateFlow<User?>(null)
    val user :StateFlow<User?> =_user

    fun loadUser(userId:String){
        viewModelScope.launch {
            _user.value=userRepository.getUserById(userId)
        }
    }
}