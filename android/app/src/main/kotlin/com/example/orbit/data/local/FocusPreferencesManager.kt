package com.example.orbit.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("focus_prefs", Context.MODE_PRIVATE)

    fun getAllowedApps(): Set<String> {
        return prefs.getStringSet("allowed_apps", emptySet()) ?: emptySet()
    }

    fun setAllowedApps(apps: Set<String>) {
        prefs.edit().putStringSet("allowed_apps", apps).apply()
    }

    fun isFocusActive(): Boolean {
        return prefs.getBoolean("is_focus_active", false)
    }

    fun setFocusActive(active: Boolean) {
        prefs.edit().putBoolean("is_focus_active", active).apply()
    }
}
