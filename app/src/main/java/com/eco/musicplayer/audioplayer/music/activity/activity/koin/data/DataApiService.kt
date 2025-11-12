package com.eco.musicplayer.audioplayer.music.activity.activity.koin.data

import com.eco.musicplayer.audioplayer.music.activity.activity.koin.ApiService
import com.eco.musicplayer.audioplayer.music.activity.model.User
//giả lập get dữ liệu từ API
class DataApiService() : ApiService {
  private  val data = listOf(
      User("Data1","Tài",25),
      User("Data2","Bình",25)
  )
    override fun getUsers(): List<User> {
           return  data
    }

    override fun getUserById(id: String): User? {
        return data.find { it.id ==id }
    }
}