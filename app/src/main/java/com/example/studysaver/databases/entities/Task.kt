package com.example.studysaver.databases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val deadline: String,
    val deadlineTimestamp: Long,
    val status: String,
    val isReadyToDelete: Boolean,
    val isChecked: Boolean,
    val dateFinished: String,
    val dateFinishedTimestamp: Long
)