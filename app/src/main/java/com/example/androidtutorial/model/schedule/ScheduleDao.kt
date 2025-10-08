package com.example.androidtutorial.model.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduleDao {
      @Insert
      suspend fun insertSchedule(schedule: Schedule)

      @Query("SELECT * FROM schedule_table WHERE dayOfWeek = :dayOfWeek")
      suspend fun getSchedulesForDay(dayOfWeek: Int): List<Schedule>

      @Query("SELECT * FROM schedule_table")
      suspend fun getAllSchedules(): List<Schedule>

      @Query("SELECT * FROM schedule_table WHERE id = :id")
      suspend fun getScheduleById(id: Int): Schedule?

      @Query("DELETE FROM schedule_table WHERE id = :id")
      suspend fun deleteScheduleById(id: Int)

      @Query("UPDATE schedule_table SET dayOfWeek = :newDayOfWeek WHERE id = :id")
      suspend fun updateDayOfWeekById(id: Int, newDayOfWeek: Int)

}