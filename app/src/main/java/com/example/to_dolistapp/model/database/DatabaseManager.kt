package com.example.to_dolistapp.model.database

import com.example.to_dolistapp.model.entities.Task
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DatabaseManager @Inject constructor(private val realm: Realm) {

    suspend fun addTask(task: Task) {
        realm.write {
            copyToRealm(task)
        }
    }

    fun getAllTasks(): Flow<List<Task>> {
        return realm.query(Task::class).asFlow().map { it.list }
    }

    fun getTaskById(taskId: String): Task? {
        return realm.query(Task::class, "id == $0", taskId).first().find()
    }

    suspend fun deleteTask(taskId: String) {
        realm.write {
            val task = query(Task::class, "id == $0", taskId).first().find()
            task?.let { delete(it) }
        }
    }
}

