package com.example.androidtutorial.model.schedule

import androidx.room.Entity
import com.example.androidtutorial.model.course.Course
import com.example.androidtutorial.model.classroom.Classroom
import com.example.androidtutorial.model.teacher.Teacher

@Entity(tableName = "schedule_table")
data class Schedule(
    val id:Int,
    val course: Course,
    val teacher: Teacher,
    val classroom: Classroom,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,

    ) {
    fun displaySchedule() {
        val days = listOf("Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật")
        println("Lịch học: ${course.courseName} - ${teacher.name}")
        println("Thời gian: ${days.getOrElse(dayOfWeek - 1) { "Unknown" }} từ $startTime đến $endTime")
        println("Địa điểm: Phòng ${classroom.roomNumber}")
    }

}