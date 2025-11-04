package com.eco.musicplayer.audioplayer.music.activity.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel

import com.eco.musicplayer.audioplayer.music.activity.model.DataClass
import com.eco.musicplayer.audioplayer.music.activity.model.Student
import com.eco.musicplayer.audioplayer.music.activity.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

//Các thành phần chính và chức năng :
//Dữ liệu (data) :Viewmodel lưu trữ các dữ liệu cần thiết cho UI.-> Thay vì lưu trong activity/frament thì lưu vào Viewmodel để tránh mất khi thay đổi cấu hinhf -> khi activity/fragment bị huỷ thì viewmodel vẫn tồn tại
//Logic nghiệp vụ: ViewModel chứa các logic liên quan đến việc xử lý dữ liệu, chẳng hạn như gọi API để lấy dữ liệu, xử lý các thao tác của người dùng và cập nhật dữ liệu.
//
//MVVM:
//View: View là phần giao diện của ứng dụng để hiển thị dữ liệu và nhận tương tác của người dùng
//Model: Chứa logic nghiệp vụ và dữ liệu của ứng dụng. Model không biết gì về View hoặc ViewModel.
//ViewModel: Lớp trung gian giữa View và Model.
class StudentViewModel : ViewModel() {
    private val _students = MutableStateFlow<Resource<List<Student>>>(Resource.Unspecified())
    val students = _students.asStateFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private val currentList = mutableListOf<Student>()

    init {
        currentList.addAll(DataClass.students)
        _students.value = Resource.Success(currentList.toList())
        Log.d("LifecycleDemo","viewmodel được tạo")
    }


    fun getStudents():List<Student>{
        return currentList.toList()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("LifecycleDemo","viewmodel bị hủy")
    }
}