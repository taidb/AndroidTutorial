package com.eco.musicplayer.audioplayer.music.activity.activity.koin

import com.eco.musicplayer.audioplayer.music.activity.model.User

interface UserRepository {
    fun getAllUsers() :List<User>
    fun addUser(user: User)
    fun getUserById(id:String):User?
}