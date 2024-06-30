package com.example.surveyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surveys")
data class Survey(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String
)
