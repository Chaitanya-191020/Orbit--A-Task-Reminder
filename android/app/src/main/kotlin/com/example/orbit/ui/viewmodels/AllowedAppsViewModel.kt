package com.example.orbit.ui.viewmodels

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbit.data.local.FocusPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppInfo(
    val packageName: String,
    val appName: String
)

@HiltViewModel
class AllowedAppsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val focusPrefs: FocusPreferencesManager
) : ViewModel() {

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps

    private val _allowedApps = MutableStateFlow<Set<String>>(emptySet())
    val allowedApps: StateFlow<Set<String>> = _allowedApps

    init {
        _allowedApps.value = focusPrefs.getAllowedApps()
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val apps = packages
                .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || it.packageName == context.packageName }
                .map {
                    AppInfo(
                        packageName = it.packageName,
                        appName = pm.getApplicationLabel(it).toString()
                    )
                }
                .sortedBy { it.appName.lowercase() }
            
            _installedApps.value = apps
        }
    }

    fun toggleAppAllowed(packageName: String): Boolean {
        val current = _allowedApps.value.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            if (current.size >= 3) {
                return false // Maximum 3 apps reached
            }
            current.add(packageName)
        }
        _allowedApps.value = current
        focusPrefs.setAllowedApps(current)
        return true
    }
}
