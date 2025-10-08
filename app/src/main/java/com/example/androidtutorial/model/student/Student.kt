package com.example.androidtutorial.model.student

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidtutorial.model.course.Course

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var age: Int,
    var address: String,
    var gpa: Double,
) {
    private val enrolledCourses = mutableListOf<Course>()

}


