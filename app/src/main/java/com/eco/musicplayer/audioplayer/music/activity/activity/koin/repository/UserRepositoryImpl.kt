package com.eco.musicplayer.audioplayer.music.activity.activity.koin.repository

import com.eco.musicplayer.audioplayer.music.activity.activity.koin.ApiService
import com.eco.musicplayer.audioplayer.music.activity.activity.koin.UserRepository
import com.eco.musicplayer.audioplayer.music.activity.model.User

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val localCache: MutableMap<String, User> = mutableMapOf()
) : UserRepository {
    override fun getAllUsers(): List<User> {
        val remoteUsers = apiService.getUsers()
        localCache.putAll(remoteUsers.associateBy { it.id })
        return remoteUsers
    }

    override fun addUser(user: User) {
        localCache[user.id] = user
    }

    override fun getUserById(id: String): User? {
        return localCache[id] ?: apiService.getUserById(id)
    }
}