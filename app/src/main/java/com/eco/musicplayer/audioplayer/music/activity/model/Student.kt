package com.eco.musicplayer.audioplayer.music.activity.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize //giusp truyền dữ liệu đi
data class Student(val name:String ,val age:Int,val address:String) : Parcelable