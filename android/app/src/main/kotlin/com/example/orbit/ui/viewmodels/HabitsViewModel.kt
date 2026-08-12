package com.example.orbit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbit.data.remote.Goal
import com.example.orbit.data.remote.Habit
import com.example.orbit.data.remote.OrbitApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val apiService: OrbitApiService
) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val habitsRes = apiService.getHabits()
                if (habitsRes.success) {
                    _habits.value = habitsRes.data
                }
                
                val goalsRes = apiService.getGoals()
                if (goalsRes.success) {
                    _goals.value = goalsRes.data
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
