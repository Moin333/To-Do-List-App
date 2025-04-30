package com.example.to_dolistapp.model.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Root : RealmObject {
    @PrimaryKey
    var id: String = "root"
    var myProjects: MyProjects? = null
    var inbox: Inbox? = null
}