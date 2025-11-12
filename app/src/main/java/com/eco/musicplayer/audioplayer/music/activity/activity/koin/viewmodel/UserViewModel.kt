package com.eco.musicplayer.audioplayer.music.activity.activity.koin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.UserRepository
import com.eco.musicplayer.audioplayer.music.activity.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) :ViewModel() {
    private val _user = MutableStateFlow<List<User>>(emptyList())
    val user:StateFlow<List<User>> =_user
    private val _loading = MutableStateFlow(false)
    val loading:StateFlow<Boolean> =_loading

    init {
        loadUser()
    }

    fun loadUser(){
        _loading.value=true
        viewModelScope.launch {
            try {
                val userList =userRepository.getAllUsers()
                _user.value=userList
            }finally {
                _loading.value=false
            }


        }

    }
    fun addUser(user: User){
        userRepository.addUser(user)
        loadUser()
    }

}