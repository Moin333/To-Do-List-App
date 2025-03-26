package com.example.to_dolistapp.di

import com.example.to_dolistapp.model.entities.Inbox
import com.example.to_dolistapp.model.entities.Inspiration
import com.example.to_dolistapp.model.entities.MyProjects
import com.example.to_dolistapp.model.entities.Project
import com.example.to_dolistapp.model.entities.Root
import com.example.to_dolistapp.model.entities.Routine
import com.example.to_dolistapp.model.entities.SubTask
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.model.entities.UserProfile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    @Singleton
    fun provideRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder(schema = setOf(
            UserProfile::class,
            SubTask::class,
            Task::class,
            Inspiration::class,
            Routine::class,
            Project::class,
            MyProjects::class,
            Inbox::class,
            Root::class
        ))
            .schemaVersion(2)
            .build()
    }

    @Provides
    @Singleton
    fun provideRealm(configuration: RealmConfiguration): Realm {
        return Realm.open(configuration)
    }
}
