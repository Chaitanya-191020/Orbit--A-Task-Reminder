package com.example.orbit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val dueDate: Long?,
    val startTime: String?,
    val endTime: String?,
    val priority: Int,
    val isCompleted: Boolean,
    val attachedAlarmId: String?,
    val loopId: String? = null,
    val loopIndex: Int = 0
)
