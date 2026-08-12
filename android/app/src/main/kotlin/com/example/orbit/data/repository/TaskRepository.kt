package com.example.orbit.data.repository

import com.example.orbit.data.local.TaskDao
import com.example.orbit.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    
    fun getTasksForAlarm(alarmId: String): Flow<List<TaskEntity>> = taskDao.getTasksForAlarm(alarmId)

    suspend fun addTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun getTaskById(taskId: String): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task.id)
    }
}
