package com.example.to_dolistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.to_dolistapp.model.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    val todoList = repository.getTasks()

    fun addTask(task: String) {
        if(task.isNotBlank()) {
            repository.addTask(task)
        }
    }

    fun removeTask(task: String) {
        repository.removeTask(task)
    }
}