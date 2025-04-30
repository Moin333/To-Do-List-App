package com.example.to_dolistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.model.entities.Project
import com.example.to_dolistapp.model.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    // Expose a StateFlow of projects from the repository.
    val projects: StateFlow<List<Project>> = repository.getProjects()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Computed property to extract project names.
    val projectNames: StateFlow<List<String>> = projects
        .map { projectList ->
            projectList.map { it.name }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /**
     * Add a new project with the given name.
     */
    fun addProject(name: String) {
        viewModelScope.launch {
            repository.addProject(name)
        }
    }

    /**
     * Add an Inspiration subfolder to the project identified by [projectId].
     */
    fun addInspirationToProject(projectId: String) {
        viewModelScope.launch {
            repository.addInspirationToProject(projectId)
        }
    }

    /**
     * Add a Routine subfolder to the project identified by [projectId].
     */
    fun addRoutineToProject(projectId: String) {
        viewModelScope.launch {
            repository.addRoutineToProject(projectId)
        }
    }
}
