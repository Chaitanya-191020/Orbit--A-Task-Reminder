package com.example.orbit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbit.alarms.AlarmScheduler
import com.example.orbit.data.local.AlarmEntity
import com.example.orbit.data.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    val alarms: StateFlow<List<AlarmEntity>> = alarmRepository.getAllAlarms()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addAlarm(
        timeString: String,
        label: String = "New Alarm",
        soundUri: String? = null,
        ringtoneName: String? = "Default",
        vibrate: Boolean = true,
        snoozeDurationMinutes: Int = 5,
        snoozeTimes: Int = 3
    ) {
        viewModelScope.launch {
            val currentAlarms = alarms.value
            if (currentAlarms.size >= 5) {
                // Keep only the 4 most recent alarms, delete the rest (which are at the start of the list if ordered by insertion)
                val alarmsToDelete = currentAlarms.sortedBy { it.createdAt }.take(currentAlarms.size - 4)
                for (oldAlarm in alarmsToDelete) {
                    alarmRepository.deleteAlarm(oldAlarm.id)
                    alarmScheduler.cancelAlarm(oldAlarm.id)
                }
            }
            
            val newAlarm = AlarmEntity(
                id = UUID.randomUUID().toString(),
                label = label,
                alarmTime = timeString,
                repeatDays = "",
                soundUri = soundUri,
                ringtoneName = ringtoneName,
                vibrate = vibrate,
                snoozeDurationMinutes = snoozeDurationMinutes,
                snoozeTimes = snoozeTimes,
                isEnabled = true
            )
            alarmRepository.addAlarm(newAlarm)
            alarmScheduler.scheduleAlarm(newAlarm.id, newAlarm.alarmTime)
        }
    }

    fun updateAlarm(
        alarmId: String,
        timeString: String,
        label: String,
        soundUri: String?,
        ringtoneName: String?,
        vibrate: Boolean,
        snoozeDurationMinutes: Int,
        snoozeTimes: Int
    ) {
        viewModelScope.launch {
            val alarmToUpdate = alarms.value.find { it.id == alarmId }
            if (alarmToUpdate != null) {
                val updatedAlarm = alarmToUpdate.copy(
                    alarmTime = timeString,
                    label = label,
                    soundUri = soundUri,
                    ringtoneName = ringtoneName,
                    vibrate = vibrate,
                    snoozeDurationMinutes = snoozeDurationMinutes,
                    snoozeTimes = snoozeTimes,
                    isEnabled = true
                )
                alarmRepository.addAlarm(updatedAlarm)
                alarmScheduler.scheduleAlarm(updatedAlarm.id, updatedAlarm.alarmTime)
            }
        }
    }

    fun toggleAlarm(alarm: AlarmEntity, isEnabled: Boolean) {
        viewModelScope.launch {
            val updatedAlarm = alarm.copy(isEnabled = isEnabled)
            alarmRepository.addAlarm(updatedAlarm) // Replaces if exists because of onConflict = REPLACE
            
            if (isEnabled) {
                alarmScheduler.scheduleAlarm(updatedAlarm.id, updatedAlarm.alarmTime)
            } else {
                alarmScheduler.cancelAlarm(updatedAlarm.id)
            }
        }
    }
}
