package com.example.androidtutorial.activity.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtutorial.activity.model.DataClass
import com.example.androidtutorial.activity.model.Student
import com.example.androidtutorial.activity.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

//ViewModel giúp tách biệt logic xử lý dữ liệu khỏi giao diện người dùng, tránh mất dữ liệu khi xoay màn hình hoặc khi Activity/Fragment bị hủy và tạo lại.
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