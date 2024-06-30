package com.example.surveyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.surveyapp.models.Survey
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Answer

/**
 * Data Access Object (DAO) for Survey, Question, and Answer entities.
 */
@Dao
interface SurveyDao {

    /**
     * Inserts a new survey into the database.
     *
     * @param survey The survey to insert.
     * @return The ID of the inserted survey.
     */
    @Insert
    suspend fun insertSurvey(survey: Survey): Long

    /**
     * Updates an existing survey in the database.
     *
     * @param survey The survey to update.
     */
    @Update
    suspend fun updateSurvey(survey: Survey)

    /**
     * Deletes a survey from the database.
     *
     * @param survey The survey to delete.
     */
    @Delete
    suspend fun deleteSurvey(survey: Survey)

    /**
     * Fetches all surveys.
     *
     * @return A list of all surveys.
     */
    @Query("SELECT * FROM surveys")
    suspend fun getAllSurveys(): List<Survey>

    /**
     * Inserts a new question into the database.
     *
     * @param question The question to insert.
     * @return The ID of the inserted question.
     */
    @Insert
    suspend fun insertQuestion(question: Question): Long

    /**
     * Updates an existing question in the database.
     *
     * @param question The question to update.
     */
    @Update
    suspend fun updateQuestion(question: Question)

    /**
     * Deletes a question from the database.
     *
     * @param question The question to delete.
     */
    @Delete
    suspend fun deleteQuestion(question: Question)

    /**
     * Fetches all questions for a specific survey.
     *
     * @param surveyId The ID of the survey.
     * @return A list of questions for the specified survey.
     */
    @Query("SELECT * FROM questions WHERE surveyId = :surveyId")
    suspend fun getQuestionsForSurvey(surveyId: Int): List<Question>

    /**
     * Inserts a new answer into the database.
     *
     * @param answer The answer to insert.
     * @return The ID of the inserted answer.
     */
    @Insert
    suspend fun insertAnswer(answer: Answer): Long

    /**
     * Fetches all answers for a specific question.
     *
     * @param questionId The ID of the question.
     * @return A list of answers for the specified question.
     */
    @Query("SELECT * FROM answers WHERE questionId = :questionId")
    suspend fun getAnswersForQuestion(questionId: Int): List<Answer>

    /**
     * Fetches a survey by ID.
     *
     * @param surveyId The ID of the survey.
     * @return The survey with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM surveys WHERE id = :surveyId")
    suspend fun getSurveyById(surveyId: Int): Survey?
}
