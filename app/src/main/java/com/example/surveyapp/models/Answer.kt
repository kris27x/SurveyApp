package com.example.surveyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class Answer(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val questionId: Int,
    val userId: Int,
    val answerValue: Int // Likert scale value (1 to 5)
)
