package com.example.orbit.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.orbit.data.local.FocusPreferencesManager
import com.example.orbit.ui.screens.focus.FocusLockActivity
import android.telecom.TelecomManager
import android.os.Handler
import android.os.Looper
import android.util.Log

class FocusLockAccessibilityService : AccessibilityService() {

    private val focusPrefs by lazy { FocusPreferencesManager(this) }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            val defaultDialer = telecomManager.defaultDialerPackage
            
            val isDialer = packageName == defaultDialer || packageName in listOf(
                "com.android.server.telecom",
                "com.google.android.dialer",
                "com.samsung.android.incallui",
                "com.samsung.android.dialer",
                "com.android.phone"
            )

            // Allow system UI, our own app, and dialer apps
            if (packageName == "com.android.systemui" || packageName == this.packageName || isDialer) return
            
            val allowedApps = focusPrefs.getAllowedApps()
            if (!allowedApps.contains(packageName)) {
                if (focusPrefs.isFocusActive()) {
                    Log.d("FocusLock", "Blocked app: $packageName")
                    // Perform back action to close the app if it was launched
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    
                    // Delay launching activity to bypass home gesture transition blocking
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, FocusLockActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        try {
                            startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("FocusLock", "Failed to start lock activity: ${e.message}")
                        }
                    }, 300)
                }
            }
        }
    }

    override fun onInterrupt() {}
    
    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        serviceInfo = info
    }
}
