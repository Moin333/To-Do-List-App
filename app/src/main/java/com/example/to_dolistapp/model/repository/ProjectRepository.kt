package com.example.to_dolistapp.model.repository

import com.example.to_dolistapp.model.entities.Inspiration
import com.example.to_dolistapp.model.entities.Project
import com.example.to_dolistapp.model.entities.Routine
import com.example.to_dolistapp.model.entities.Root
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(private val realm: Realm) {

    /**
     * Returns a Flow of all projects from the Root's myProjects.
     * Assumes that there is a single Root object with id "root".
     */
    fun getProjects(): Flow<List<Project>> {
        // Query the Root object and then access its myProjects.projects list.
        return realm.query(Root::class, "id == $0", "root")
            .asFlow()
            .map { result ->
                val root = result.list.firstOrNull()
                root?.myProjects?.projects.orEmpty()
            }
    }

    /**
     * Adds a new project with the provided name.
     */
    suspend fun addProject(name: String) {
        realm.write {
            // Create a new Project
            val newProject = copyToRealm(Project().apply {
                this.name = name
            })

            // Retrieve the Root object and add the new project to MyProjects.
            val root = query(Root::class, "id == $0", "root").first().find()
            if (root != null) {
                // If myProjects doesn't exist, create one.
                if (root.myProjects == null) {
                    root.myProjects = copyToRealm(com.example.to_dolistapp.model.entities.MyProjects())
                }
                root.myProjects?.projects?.add(newProject)
            }
        }
    }

    /**
     * Adds an Inspiration subfolder to the given project if not already present.
     */
    suspend fun addInspirationToProject(projectId: String) {
        realm.write {
            val project = query(Project::class, "id == $0", projectId).first().find()
            project?.let {
                if (it.inspiration == null) {
                    it.inspiration = copyToRealm(Inspiration())
                }
            }
        }
    }

    /**
     * Adds a Routine subfolder to the given project if not already present.
     */
    suspend fun addRoutineToProject(projectId: String) {
        realm.write {
            val project = query(Project::class, "id == $0", projectId).first().find()
            project?.let {
                if (it.routine == null) {
                    it.routine = copyToRealm(Routine())
                }
            }
        }
    }
}
