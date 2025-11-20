package com.eco.musicplayer.audioplayer.music.activity.contentprovider

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsersCursor(): Cursor

    @Insert
    fun insertUser(user: User)
}
