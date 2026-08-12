package com.example.orbit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbit.data.local.TaskEntity
import com.example.orbit.data.repository.TaskRepository
import com.example.orbit.alarms.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    data class TaskData(val title: String, val startTime: String, val endTime: String)

    val tasks: StateFlow<List<TaskEntity>> = taskRepository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.addTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun addNewTask(title: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            val taskId = java.util.UUID.randomUUID().toString()
            val newTask = TaskEntity(
                id = taskId,
                title = title,
                description = "",
                dueDate = System.currentTimeMillis(),
                startTime = startTime.takeIf { it.isNotBlank() },
                endTime = endTime.takeIf { it.isNotBlank() },
                priority = 0,
                isCompleted = false,
                attachedAlarmId = null
            )
            taskRepository.addTask(newTask)
            
            if (startTime.isNotBlank() && endTime.isNotBlank()) {
                val startMillis = alarmScheduler.parseTimeMillis(startTime)
                val endMillis = alarmScheduler.parseTimeMillis(endTime)
                if (startMillis > 0 && endMillis > 0) {
                    alarmScheduler.scheduleTaskCountdownAlarm(taskId, startMillis, endMillis)
                }
            }
        }
    }

    fun addLoopedTasks(tasksList: List<TaskData>) {
        if (tasksList.isEmpty()) return
        viewModelScope.launch {
            val loopId = java.util.UUID.randomUUID().toString()
            tasksList.forEachIndexed { index, taskData ->
                val taskId = java.util.UUID.randomUUID().toString()
                val newTask = TaskEntity(
                    id = taskId,
                    title = taskData.title,
                    description = "",
                    dueDate = System.currentTimeMillis(),
                    startTime = taskData.startTime.takeIf { it.isNotBlank() },
                    endTime = taskData.endTime.takeIf { it.isNotBlank() },
                    priority = 0,
                    isCompleted = false,
                    attachedAlarmId = null,
                    loopId = if (tasksList.size > 1) loopId else null,
                    loopIndex = index
                )
                taskRepository.addTask(newTask)
                
                if (taskData.startTime.isNotBlank() && taskData.endTime.isNotBlank()) {
                    val startMillis = alarmScheduler.parseTimeMillis(taskData.startTime)
                    val endMillis = alarmScheduler.parseTimeMillis(taskData.endTime)
                    if (startMillis > 0 && endMillis > 0) {
                        alarmScheduler.scheduleTaskCountdownAlarm(taskId, startMillis, endMillis)
                    }
                }
            }
        }
    }

    suspend fun getTaskById(taskId: String): TaskEntity? {
        return taskRepository.getTaskById(taskId)
    }

    fun updateTask(taskId: String, title: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            if (task != null) {
                val updatedTask = task.copy(
                    title = title,
                    startTime = startTime.takeIf { it.isNotBlank() },
                    endTime = endTime.takeIf { it.isNotBlank() }
                )
                taskRepository.addTask(updatedTask)
                
                // Update alarms if time changed
                if (startTime.isNotBlank() && endTime.isNotBlank()) {
                    val startMillis = alarmScheduler.parseTimeMillis(startTime)
                    val endMillis = alarmScheduler.parseTimeMillis(endTime)
                    if (startMillis > 0 && endMillis > 0) {
                        alarmScheduler.scheduleTaskCountdownAlarm(taskId, startMillis, endMillis)
                    }
                }
            }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            // If the task has an active alarm, we might want to cancel it,
            // but for simplicity we assume alarmScheduler doesn't explicitly expose cancelTaskAlarm right now.
        }
    }
}
