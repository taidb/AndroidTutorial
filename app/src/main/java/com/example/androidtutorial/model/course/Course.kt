package com.example.androidtutorial.model.course

import androidx.room.Entity

@Entity(tableName = "course_table")
class Course(
    val courseId: Int,
    val courseName: String,
    val credit: Int
) {
    fun displayCourse() {
        println("Course: $courseName Id:  $courseId, Credit: $credit")
    }
}