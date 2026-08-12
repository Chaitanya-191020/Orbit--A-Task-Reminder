package com.example.orbit.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.orbit.ui.screens.alarm.AlarmRingingActivity
import com.example.orbit.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var alarmRepository: AlarmRepository
    
    override fun onReceive(context: Context, intent: Intent) {
        val isTaskCountdown = intent.getBooleanExtra("IS_TASK_COUNTDOWN", false)
        
        if (isTaskCountdown) {
            val taskId = intent.getStringExtra("TASK_ID") ?: return
            val endTimeMillis = intent.getLongExtra("END_TIME_MILLIS", 0L)
            
            val serviceIntent = Intent(context, com.example.orbit.services.CountdownOverlayService::class.java).apply {
                putExtra("TASK_ID", taskId)
                putExtra("END_TIME_MILLIS", endTimeMillis)
            }
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            } catch (e: Exception) {
                android.util.Log.e("AlarmReceiver", "Failed to start foreground service: ${e.message}")
            }
            return
        }

        val alarmId = intent.getStringExtra("ALARM_ID") ?: return
        Log.d("AlarmReceiver", "Alarm Triggered! ID: $alarmId")
        
        var soundUri: String? = null
        var vibrate = true
        var snoozeDurationMinutes = 5
        var snoozeTimes = 3
        var snoozedCount = 0
        var label = "Alarm"

        runBlocking {
            val alarm = alarmRepository.getAlarmById(alarmId)
            if (alarm != null) {
                soundUri = alarm.soundUri
                vibrate = alarm.vibrate
                snoozeDurationMinutes = alarm.snoozeDurationMinutes
                snoozeTimes = alarm.snoozeTimes
                snoozedCount = alarm.snoozedCount
                label = alarm.label
            }
        }

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            action = AlarmService.ACTION_START
            putExtra(AlarmService.EXTRA_ALARM_ID, alarmId)
            putExtra("SOUND_URI", soundUri)
            putExtra("VIBRATE", vibrate)
            putExtra("SNOOZE_DURATION", snoozeDurationMinutes)
            putExtra("SNOOZE_TIMES", snoozeTimes)
            putExtra("SNOOZED_COUNT", snoozedCount)
            putExtra("LABEL", label)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
