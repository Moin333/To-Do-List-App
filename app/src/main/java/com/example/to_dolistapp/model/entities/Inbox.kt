package com.example.to_dolistapp.model.entities

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Inbox : RealmObject {
    @PrimaryKey
    var id: String = "inbox"
    var tasks: RealmList<Task> = realmListOf() // All tasks irrespective of their projects or categories
}