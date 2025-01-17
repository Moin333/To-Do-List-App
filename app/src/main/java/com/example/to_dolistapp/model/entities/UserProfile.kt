package com.example.to_dolistapp.model.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class UserProfile: RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var userName: String = ""
    var photo: String? = null
    var email: String = ""
}