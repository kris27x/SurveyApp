package com.example.surveyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a Survey entity in the database.
 *
 * @property id The unique ID of the survey.
 * @property title The title of the survey.
 * @property description The description of the survey.
 */
@Entity(tableName = "surveys")
data class Survey(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
)
