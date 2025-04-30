package com.example.to_dolistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.model.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    // Expose a flow of tasks based on a search query.
    fun searchTasks(query: String): Flow<List<Task>> {
        return repository.searchTasks(query)
    }
}
