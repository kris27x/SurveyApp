package com.example.surveyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a Question entity in the database.
 *
 * @property id The unique ID of the question.
 * @property surveyId The ID of the survey to which this question belongs.
 * @property text The text of the question.
 */
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surveyId: Int,
    val text: String
)
