package com.eco.musicplayer.audioplayer.music.activity.model

import java.io.Serializable

//Serialization là quá trình chuyển các cấu trúc dữ liệu thành 1 định dạng có thể lưu trữ được
class Staff(val id: Int, val name: String, var age: Int, var salary: Double) : Serializable {
}//việc thực hiện Serializable gọn hơn Parcelize