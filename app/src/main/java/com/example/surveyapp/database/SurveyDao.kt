package com.example.surveyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.surveyapp.models.Survey
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Answer

@Dao
interface SurveyDao {
    @Insert
    suspend fun insertSurvey(survey: Survey): Long

    @Update
    suspend fun updateSurvey(survey: Survey)

    @Delete
    suspend fun deleteSurvey(survey: Survey)

    @Query("SELECT * FROM surveys")
    suspend fun getAllSurveys(): List<Survey>

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions WHERE surveyId = :surveyId")
    suspend fun getQuestionsForSurvey(surveyId: Int): List<Question>

    @Insert
    suspend fun insertAnswer(answer: Answer): Long

    @Query("SELECT * FROM answers WHERE questionId = :questionId")
    suspend fun getAnswersForQuestion(questionId: Int): List<Answer>
}
