package com.example.to_dolistapp.model.entities

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID
import java.time.LocalDate

class Task : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var title: String = ""
    var description: String? = null
    var projectName: String? = null
    var labels: RealmList<String> = realmListOf()
    var priority: Int = 0 // 1: P1, 2: P2, 3: P3, 4: P4
    var date: RealmInstant? = null
    var assignee: String? = null
    var reminders: RealmList<RealmInstant> = realmListOf()
    var deadline: RealmInstant? = null
    var location: String? = null
    var subTasks: RealmList<SubTask> = realmListOf()
    var isCompleted: Boolean = false
}

fun RealmInstant.toLocalDate(): LocalDate {
    return LocalDate.ofEpochDay(this.epochSeconds / (24 * 60 * 60))
}