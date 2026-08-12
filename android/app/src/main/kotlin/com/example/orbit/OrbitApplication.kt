package com.example.orbit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OrbitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Create Alarm Notification Channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "ALARM_CHANNEL_MAX",
                "High Priority Alarms",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used to display alarms"
                setBypassDnd(true)
                // Don't set sound/vibration here if we manage it in Activity, but it's good practice
            }
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
