package com.example.surveyapp.models

/**
 * Data class representing a Question entity in the database.
 *
 * @property id The unique ID of the question.
 * @property surveyId The ID of the survey to which this question belongs.
 * @property text The text of the question.
 */
data class Question(
    val id: Int = 0,
    var surveyId: Int,
    val text: String
)
