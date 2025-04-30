package com.example.to_dolistapp.model.entities

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class MyProjects : RealmObject {
    @PrimaryKey
    var id: String = "my_projects"
    var projects: RealmList<Project> = realmListOf() // List of all projects
}