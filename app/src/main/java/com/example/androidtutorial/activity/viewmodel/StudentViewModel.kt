package com.example.androidtutorial.activity.viewmodel


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

class StudentViewModel : ViewModel() {
    private val _students = MutableStateFlow<Resource<List<Student>>>(Resource.Unspecified())
    val students = _students.asStateFlow()
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private val currentList = mutableListOf<Student>()

    init {
        currentList.addAll(DataClass.students)
        _students.value = Resource.Success(currentList.toList())
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            _students.emit(Resource.Loading())

            try {
                currentList.add(student)
                _students.emit(Resource.Success(currentList.toList()))
            } catch (e: Exception) {
                _error.emit("Không thể thêm sinh viên: ${e.message}")
                _students.emit(Resource.Error("Thêm thất bại"))
            }
        }
    }
    fun getStudents():List<Student>{
        return currentList.toList()
    }
}