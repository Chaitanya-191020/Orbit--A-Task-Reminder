package com.example.orbit.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.CountDownTimer
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.orbit.ui.screens.focus.FocusLockActivity

class CountdownOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private var countdownTimer: CountDownTimer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(2, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED)
        } else {
            startForeground(2, createNotification())
        }
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskId = intent?.getStringExtra("TASK_ID") ?: ""
        val endTimeMillis = intent?.getLongExtra("END_TIME_MILLIS", 0L) ?: 0L
        
        showOverlay(taskId, endTimeMillis)
        return START_NOT_STICKY
    }

    private fun showOverlay(taskId: String, endTimeMillis: Long) {
        if (overlayView != null) return

        val textView = TextView(this).apply {
            text = "10"
            textSize = 120f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setShadowLayer(10f, 0f, 0f, Color.BLACK)
        }
        overlayView = textView

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        try {
            windowManager.addView(overlayView, params)
        } catch (e: Exception) {
            Log.e("CountdownService", "Failed to add overlay view: ${e.message}")
            // Even if overlay fails (no permission), we can still trigger the lock after 10s
        }

        countdownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) + 1
                textView.text = seconds.toString()
            }

            override fun onFinish() {
                removeOverlay()
                startFocusLockActivity(taskId, endTimeMillis)
                stopSelf()
            }
        }.start()
    }

    private fun startFocusLockActivity(taskId: String, endTimeMillis: Long) {
        val intent = Intent(this, FocusLockActivity::class.java).apply {
            putExtra("TASK_ID", taskId)
            putExtra("END_TIME_MILLIS", endTimeMillis)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun removeOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel()
        removeOverlay()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "countdown_channel",
            "Countdown Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "countdown_channel")
            .setContentTitle("Focus Mode Starting")
            .setContentText("A task is about to begin")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }
}
