package com.example.to_dolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.model.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    // Use the raw flow of all tasks from the repository.
    private val _allTasks = repository.getAllTasks()

    // Expose active tasks (not completed)
    val activeTasks: StateFlow<List<Task>> = _allTasks
        .map { tasks -> tasks.filter { !it.isCompleted } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Expose completed tasks (if needed in ActivityLogScreen, etc.)
    val completedTasks: StateFlow<List<Task>> = _allTasks
        .map { tasks -> tasks.filter { it.isCompleted } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun completeTask(taskId: String) {
        viewModelScope.launch {
            repository.markTaskComplete(taskId)
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

    fun updateTaskFields(taskId: String, newTitle: String, newDescription: String) {
        viewModelScope.launch {
            repository.updateTaskFields(taskId, newTitle, newDescription)
        }
    }
}
