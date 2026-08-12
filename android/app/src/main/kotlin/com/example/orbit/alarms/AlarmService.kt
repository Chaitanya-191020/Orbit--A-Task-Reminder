package com.example.orbit.alarms

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.orbit.R
import com.example.orbit.ui.screens.alarm.AlarmRingingActivity
import com.example.orbit.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import android.net.Uri

@AndroidEntryPoint
class AlarmService : Service() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler
    
    @Inject
    lateinit var alarmRepository: AlarmRepository

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_DISMISS = "ACTION_DISMISS"
        const val ACTION_SNOOZE = "ACTION_SNOOZE"
        const val ACTION_SILENCE = "ACTION_SILENCE"
        const val EXTRA_ALARM_ID = "ALARM_ID"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val alarmId = intent?.getStringExtra(EXTRA_ALARM_ID) ?: "Unknown"

        Log.d("AlarmService", "Action: $action, ID: $alarmId")

        when (action) {
            ACTION_START -> startAlarm(intent)
            ACTION_DISMISS -> stopAlarm()
            ACTION_SNOOZE -> snoozeAlarm(alarmId)
            ACTION_SILENCE -> silenceAlarm()
        }

        return START_NOT_STICKY
    }

    private fun startAlarm(intent: Intent?) {
        val alarmId = intent?.getStringExtra(EXTRA_ALARM_ID) ?: "Unknown"
        val soundUriStr = intent?.getStringExtra("SOUND_URI")
        val vibrate = intent?.getBooleanExtra("VIBRATE", true) ?: true
        val label = intent?.getStringExtra("LABEL") ?: "Alarm Ringing"
        val snoozeTimes = intent?.getIntExtra("SNOOZE_TIMES", 3) ?: 3
        val snoozedCount = intent?.getIntExtra("SNOOZED_COUNT", 0) ?: 0
        
        val ringtoneUri = if (!soundUriStr.isNullOrEmpty()) Uri.parse(soundUriStr) else RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(this@AlarmService, ringtoneUri)
                isLooping = true
                prepare()
                start()
            } catch (e: Exception) {
                Log.e("AlarmService", "Failed to play ringtone: ${e.message}")
            }
        }

        if (vibrate) {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500, 500), 0))
            } else {
                vibrator?.vibrate(longArrayOf(0, 500, 500), 0)
            }
        }

        val ringingIntent = Intent(this, AlarmRingingActivity::class.java).apply {
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra("LABEL", label)
            putExtra("SNOOZE_LIMIT_REACHED", snoozedCount >= snoozeTimes)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        
        // Attempt to start the activity directly. This will succeed if the app is in the foreground,
        // and will fail silently (or be blocked) in the background on Android 10+, falling back to the Heads-Up notification.
        try {
            startActivity(ringingIntent)
        } catch (e: Exception) {
            Log.e("AlarmService", "Could not start activity directly: ${e.message}")
        }

        val fullScreenIntent = PendingIntent.getActivity(
            this,
            alarmId.hashCode(),
            ringingIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_DISMISS
            putExtra(EXTRA_ALARM_ID, alarmId)
        }
        val dismissPendingIntent = PendingIntent.getService(this, alarmId.hashCode() + 1, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val snoozeIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_ALARM_ID, alarmId)
        }
        val snoozePendingIntent = PendingIntent.getService(this, alarmId.hashCode() + 2, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "ALARM_CHANNEL_MAX")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(label)
            .setContentText("Tap to open or swipe to dismiss")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenIntent, true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent)
            .addAction(android.R.drawable.ic_popup_sync, "Snooze", snoozePendingIntent)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(12345, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED)
        } else {
            startForeground(12345, notification)
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        vibrator?.cancel()
        
        // Broadcast to close AlarmRingingActivity if it's open
        sendBroadcast(Intent("ACTION_CLOSE_ALARM_ACTIVITY"))
        
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun snoozeAlarm(alarmId: String) {
        runBlocking {
            val alarm = alarmRepository.getAlarmById(alarmId)
            if (alarm != null) {
                if (alarm.snoozedCount < alarm.snoozeTimes) {
                    val updatedAlarm = alarm.copy(snoozedCount = alarm.snoozedCount + 1)
                    alarmRepository.addAlarm(updatedAlarm)
                    alarmScheduler.scheduleSnooze(alarmId, alarm.snoozeDurationMinutes)
                }
            }
        }
        stopAlarm()
    }

    private fun silenceAlarm() {
        mediaPlayer?.stop()
        vibrator?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        vibrator?.cancel()
    }
}
