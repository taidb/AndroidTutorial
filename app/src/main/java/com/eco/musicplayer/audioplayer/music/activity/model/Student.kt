package com.eco.musicplayer.audioplayer.music.activity.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize // thường được sự dụng để gửi dữ liệu qua Bunble gửi cùng Intent
data class Student(val name: String, val age: Int, val address: String) : Parcelable

//tuần tự hóa và giải tuần tự hóa đối tượng hiểu quả,giúp giao diện nhanh hơn  Serialzable trong việc truyền các đối tượng
//Nó yêu cầu overriding writeToParcel() để ghi dữ liệu vào Parcel và tái tạo lại từ Parcel