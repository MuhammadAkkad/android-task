package com.company.androidtask.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Tasks")
data class TasksModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val task: String,
    val title: String,
    val description: String,
    val sort: String,
    val wageType: String,
    val businessUnitKey: String,
    val businessUnit: String,
    val parentTaskID: String,
    val preplanningBoardQuickSelect: String?,
    val colorCode: String,
    val workingTime: String?,
    val isAvailableInTimeTrackingKioskMode: Boolean,
    val isAbstract: Boolean
)
