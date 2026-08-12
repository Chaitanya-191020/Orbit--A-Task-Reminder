package com.example.orbit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbit.data.remote.AnalyticsData
import com.example.orbit.data.remote.OrbitApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val apiService: OrbitApiService
) : ViewModel() {

    private val _analyticsData = MutableStateFlow<AnalyticsData?>(null)
    val analyticsData: StateFlow<AnalyticsData?> = _analyticsData

    init {
        fetchAnalytics()
    }

    private fun fetchAnalytics() {
        viewModelScope.launch {
            try {
                val response = apiService.getAnalyticsSummary()
                if (response.success) {
                    _analyticsData.value = response.data
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
