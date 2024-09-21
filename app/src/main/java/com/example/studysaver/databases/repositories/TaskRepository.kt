package com.example.studysaver.databases.repositories

import androidx.lifecycle.LiveData
import com.example.studysaver.databases.dao.TaskDao
import com.example.studysaver.databases.entities.Task

class TaskRepository(private val taskDao: TaskDao) {
    fun getTaskByStatus(status: String): LiveData<List<Task>> {
        return taskDao.getTaskByStatus(status)
    }

    fun getTaskCountByStatus(status: String): LiveData<Int> {
        return taskDao.getTaskCountByStatus(status)
    }

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun updateTaskById(
        taskId: Long,
        title: String,
        description: String,
        deadline: String,
        deadlineTimestamp: Long
    ) {
        taskDao.updateTaskById(taskId, title, description, deadline, deadlineTimestamp)
    }

    suspend fun updateTaskReadyToDeleteStatus(deleteStatus: Boolean, taskStatus: String) {
        taskDao.updateTaskReadyToDeleteStatus(deleteStatus, taskStatus)
    }

    suspend fun updateTaskCheckedStatus(isChecked: Boolean, taskStatus: String) {
        taskDao.updateTaskCheckedStatus(isChecked, taskStatus)
    }

    suspend fun updateTaskStatus(taskId: Long, dateFinished: String) {
        taskDao.updateTaskStatus(taskId, dateFinished)
    }

    suspend fun updateTaskStatusIfDeadlinePassed(currentTime: Long) {
        taskDao.updateTaskStatusIfDeadlinePassed(currentTime)
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }

    suspend fun deleteTaskByStatus(status: String) {
        taskDao.deleteTaskByStatus(status)
    }
}