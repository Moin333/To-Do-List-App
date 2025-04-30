package com.example.to_dolistapp.model.entities

import io.realm.kotlin.types.EmbeddedRealmObject

class SubTask : EmbeddedRealmObject {
    var title: String = ""
    var description: String? = null
    var isCompleted: Boolean = false
}