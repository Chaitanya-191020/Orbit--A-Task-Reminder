package com.example.orbit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlarmEntity::class, TaskEntity::class], version = 5, exportSchema = false)
abstract class OrbitDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun taskDao(): TaskDao
}
