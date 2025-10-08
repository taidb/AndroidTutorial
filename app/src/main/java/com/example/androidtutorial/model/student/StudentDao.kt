package com.example.androidtutorial.model.student

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Query("SELECT * FROM student_table")
    suspend fun getAllStudents(): List<Student>

    @Query("SELECT * FROM student_table WHERE id = :id")
    suspend fun getStudentById(id: Int): Student?

    @Query("DELETE FROM student_table WHERE id = :id")
    suspend fun deleteStudentById(id: Int)

    @Query("UPDATE student_table SET name = :newName WHERE id = :id")
    suspend fun updateNameById(id: Int, newName: String)

}