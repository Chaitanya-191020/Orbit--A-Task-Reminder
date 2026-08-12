package com.example.orbit.data.repository

import com.example.orbit.data.local.AlarmDao
import com.example.orbit.data.local.AlarmEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao
) {
    fun getAllAlarms(): Flow<List<AlarmEntity>> = alarmDao.getAllAlarms()
    
    suspend fun getAlarmById(id: String): AlarmEntity? = alarmDao.getAlarmById(id)
    
    suspend fun addAlarm(alarm: AlarmEntity) {
        alarmDao.insertAlarm(alarm)
    }
    
    suspend fun deleteAlarm(alarmId: String) {
        alarmDao.deleteAlarm(alarmId)
    }
}
