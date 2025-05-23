package com.company.androidtask.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.androidtask.data.remote.model.TasksModel

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TasksModel>)

    @Query("SELECT * FROM Tasks")
    suspend fun getTasks(): List<TasksModel>

    @Query("DELETE FROM Tasks")
    suspend fun nukeTasks()
}