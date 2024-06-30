package com.example.surveyapp.repositories

import com.example.surveyapp.database.SurveyDao
import com.example.surveyapp.models.Survey
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Answer

/**
 * Repository for managing survey data operations.
 *
 * @property surveyDao The DAO for accessing survey data.
 */
class SurveyRepository(private val surveyDao: SurveyDao) {

    /**
     * Inserts a new survey.
     *
     * @param survey The survey to insert.
     * @return The ID of the inserted survey.
     */
    suspend fun insertSurvey(survey: Survey): Long {
        return surveyDao.insertSurvey(survey)
    }

    /**
     * Updates an existing survey.
     *
     * @param survey The survey to update.
     */
    suspend fun updateSurvey(survey: Survey) {
        surveyDao.updateSurvey(survey)
    }

    /**
     * Deletes a survey.
     *
     * @param survey The survey to delete.
     */
    suspend fun deleteSurvey(survey: Survey) {
        surveyDao.deleteSurvey(survey)
    }

    /**
     * Fetches all surveys.
     *
     * @return A list of all surveys.
     */
    suspend fun getAllSurveys(): List<Survey> {
        return surveyDao.getAllSurveys()
    }

    /**
     * Inserts a new question.
     *
     * @param question The question to insert.
     * @return The ID of the inserted question.
     */
    suspend fun insertQuestion(question: Question): Long {
        return surveyDao.insertQuestion(question)
    }

    /**
     * Updates an existing question.
     *
     * @param question The question to update.
     */
    suspend fun updateQuestion(question: Question) {
        surveyDao.updateQuestion(question)
    }

    /**
     * Deletes a question.
     *
     * @param question The question to delete.
     */
    suspend fun deleteQuestion(question: Question) {
        surveyDao.deleteQuestion(question)
    }

    /**
     * Fetches all questions for a specific survey.
     *
     * @param surveyId The ID of the survey.
     * @return A list of questions for the specified survey.
     */
    suspend fun getQuestionsForSurvey(surveyId: Int): List<Question> {
        return surveyDao.getQuestionsForSurvey(surveyId)
    }

    /**
     * Inserts a new answer.
     *
     * @param answer The answer to insert.
     * @return The ID of the inserted answer.
     */
    suspend fun insertAnswer(answer: Answer): Long {
        return surveyDao.insertAnswer(answer)
    }

    /**
     * Fetches all answers for a specific question.
     *
     * @param questionId The ID of the question.
     * @return A list of answers for the specified question.
     */
    suspend fun getAnswersForQuestion(questionId: Int): List<Answer> {
        return surveyDao.getAnswersForQuestion(questionId)
    }
}
