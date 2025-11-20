package com.eco.musicplayer.audioplayer.music.activity.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contacts_table")
data class Contact(

    @PrimaryKey(autoGenerate = true)
    val contact_id:Int=0,

    var contact_name:String,

    var contact_email:String
)
