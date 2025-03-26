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

    fun searchTasks(query: String): Flow<List<Task>> {
        // Return all tasks if the query is blank
        if (query.isBlank()) {
            return getAllTasks()
        }

        // Otherwise, search by title or description
        return databaseManager.searchTasks(query)
    }

    suspend fun markTaskComplete(taskId: String) {
        databaseManager.markTaskComplete(taskId)
    }

    suspend fun deleteTask(taskId: String) {
        databaseManager.deleteTask(taskId)
    }

    suspend fun updateTaskFields(taskId: String, newTitle: String, newDescription: String) {
        databaseManager.updateTaskFields(taskId, newTitle, newDescription)
    }

}
