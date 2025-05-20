package com.company.androidtask.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.company.androidtask.data.remote.model.TasksModel

@Database(
    entities = [TasksModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

}