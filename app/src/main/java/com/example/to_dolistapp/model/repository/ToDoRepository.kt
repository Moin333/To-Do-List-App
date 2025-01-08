package com.example.to_dolistapp.model.repository

import androidx.compose.runtime.mutableStateListOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToDoRepository @Inject constructor() {

    private val taskList = mutableStateListOf<String>()

    fun getTasks() = taskList

    fun addTask(task: String) {
        if (task.isNotBlank()) {
            taskList.add(task)
        }
    }

    fun removeTask(task: String) {
        taskList.remove(task)
    }
}
