package com.example.androidtutorial.model.teacher

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidtutorial.model.course.Course

@Entity(tableName = "teacher_table")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var age: Int,
    var subject: String,
    var salary: Double? = null
){
    private val taughtCourses = mutableListOf<Course>()
}


