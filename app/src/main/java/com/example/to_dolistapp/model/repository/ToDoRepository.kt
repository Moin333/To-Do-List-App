package com.example.to_dolistapp.model.repository

import com.example.to_dolistapp.model.database.DatabaseManager
import com.example.to_dolistapp.model.entities.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToDoRepository @Inject constructor(private val databaseManager: DatabaseManager) {

    fun getAllTasks(): Flow<List<Task>> {
        return databaseManager.getAllTasks()
    }

    suspend fun addTask(task: Task) {
        databaseManager.addTask(task)
    }

    fun getTaskById(taskId: String): Task? {
        return databaseManager.getTaskById(taskId)
    }

    suspend fun deleteTask(taskId: String) {
        databaseManager.deleteTask(taskId)
    }
}
