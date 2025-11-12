package com.eco.musicplayer.audioplayer.music.activity.activity.koin

import com.eco.musicplayer.audioplayer.music.activity.model.User

interface ApiService  {
    fun getUsers():List<User>
    fun getUserById(id:String):User?
}