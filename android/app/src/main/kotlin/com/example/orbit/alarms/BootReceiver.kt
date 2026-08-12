package com.example.orbit.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            Log.d("BootReceiver", "Boot completed or package replaced. Rescheduling alarms...")
            // Logic to fetch alarms from Room and reschedule via AlarmManager would go here.
        }
    }
}
