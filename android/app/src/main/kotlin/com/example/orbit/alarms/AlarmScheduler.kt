package com.example.orbit.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(alarmId: String, timeString: String) {
        // timeString format expected: "hh:mm a" e.g., "08:00 AM" or "13:18"
        try {
            val calendar = Calendar.getInstance()
            val format = if (timeString.contains("AM") || timeString.contains("PM")) {
                SimpleDateFormat("hh:mm a", Locale.getDefault())
            } else {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            }
            
            val parsedTime = format.parse(timeString) ?: return
            
            val timeCalendar = Calendar.getInstance().apply {
                time = parsedTime
            }

            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            // If time is in the past, schedule for next day
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("ALARM_ID", alarmId)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Need SCHEDULE_EXACT_ALARM permission in Android 12+
            // We use setAlarmClock to show the alarm icon in status bar and bypass Doze
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                // Fallback or request permission logic here
            } else {
                val alarmClockInfo = AlarmManager.AlarmClockInfo(
                    calendar.timeInMillis,
                    pendingIntent // Intent to launch if user clicks the status bar icon (can be MainActivity)
                )
                
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            }
            
            Log.d("AlarmScheduler", "Alarm $alarmId scheduled for ${calendar.time}")
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "Failed to schedule alarm: ${e.message}")
        }
    }

    fun scheduleSnooze(alarmId: String, durationMinutes: Int) {
        try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MINUTE, durationMinutes)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("ALARM_ID", alarmId)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (alarmId + "_snooze").hashCode(), // different hash so it doesn't conflict with main alarm
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                // Fallback
            } else {
                val alarmClockInfo = AlarmManager.AlarmClockInfo(
                    calendar.timeInMillis,
                    pendingIntent
                )
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            }
            Log.d("AlarmScheduler", "Alarm $alarmId snoozed until ${calendar.time}")
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "Failed to schedule snooze: ${e.message}")
        }
    }

    fun scheduleTaskCountdownAlarm(taskId: String, startTimeMillis: Long, endTimeMillis: Long) {
        try {
            val triggerTime = startTimeMillis - 10000 // 10 seconds before
            
            if (triggerTime <= System.currentTimeMillis()) return
            
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("TASK_ID", taskId)
                putExtra("IS_TASK_COUNTDOWN", true)
                putExtra("END_TIME_MILLIS", endTimeMillis)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                ("task_$taskId").hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmClockInfo = AlarmManager.AlarmClockInfo(
                triggerTime,
                pendingIntent
            )
            
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            
            Log.d("AlarmScheduler", "Task Countdown $taskId scheduled for $triggerTime")
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "Failed to schedule task countdown: ${e.message}")
        }
    }

    fun parseTimeMillis(timeString: String): Long {
        try {
            val calendar = Calendar.getInstance()
            val format = if (timeString.contains("AM") || timeString.contains("PM")) {
                SimpleDateFormat("hh:mm a", Locale.getDefault())
            } else {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            }
            
            val parsedTime = format.parse(timeString) ?: return 0L
            
            val timeCalendar = Calendar.getInstance().apply {
                time = parsedTime
            }

            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            return calendar.timeInMillis
        } catch (e: Exception) {
            return 0L
        }
    }

    fun cancelAlarm(alarmId: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d("AlarmScheduler", "Alarm $alarmId canceled")
    }
}
