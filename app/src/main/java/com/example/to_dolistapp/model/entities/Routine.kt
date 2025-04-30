package com.example.to_dolistapp.model.entities

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Routine : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var tasks: RealmList<Task> = realmListOf()
}