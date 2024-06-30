package com.example.surveyapp.models

/**
 * Data class representing a Survey entity in the database.
 *
 * @property id The unique ID of the survey.
 * @property title The title of the survey.
 * @property description The description of the survey.
 */
data class Survey(
    val id: Int = 0,
    val title: String,
    val description: String
)
