package com.example.to_dolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.model.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    // StateFlow to hold the list of tasks
    private val _todoList = MutableStateFlow<List<Task>>(emptyList())
    val todoList: StateFlow<List<Task>> get() = _todoList

    init {
        // Collect tasks from the repository and update the StateFlow
        viewModelScope.launch {
            repository.getAllTasks().collect { tasks ->
                _todoList.value = tasks
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun removeTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun getTaskById(taskId: String): Task? {
        return repository.getTaskById(taskId)
    }
}
