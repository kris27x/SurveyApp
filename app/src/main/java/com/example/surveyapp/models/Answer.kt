package com.example.surveyapp.models

/**
 * Data class representing an Answer entity in the database.
 *
 * @property id The unique ID of the answer.
 * @property questionId The ID of the question to which this answer belongs.
 * @property userId The ID of the user who provided this answer.
 * @property answerValue The value of the answer on a Likert scale (1 to 5).
 */
data class Answer(
    val id: Int = 0,
    val questionId: Int,
    val userId: Int,
    val answerValue: Int // Likert scale value (1 to 5)
)
