package com.example.orbit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey
    val id: String,
    val label: String,
    val alarmTime: String,
    val repeatDays: String,
    val soundUri: String?,
    val ringtoneName: String? = "Default",
    val vibrate: Boolean,
    val snoozeDurationMinutes: Int,
    val snoozeTimes: Int = 3,
    val snoozedCount: Int = 0,
    val isEnabled: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)
