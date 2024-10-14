package com.example.to_dolistapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ToDoViewModel: ViewModel() {
    var todoList = mutableStateListOf<String>()
        private set

    fun addTask(task: String) {
        if(task.isNotBlank()) {
            todoList.add(task)
        }
    }

    fun removeTask(task: String) {
        todoList.remove(task)
    }
}