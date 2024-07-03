package com.example.surveyapp.repositories

import android.content.Context
import android.content.ContentValues
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Survey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for managing survey data operations.
 *
 * @property dbHelper The database helper for accessing survey data.
 */
class SurveyRepository private constructor(private val dbHelper: SurveyDatabaseHelper) {

    companion object {
        @Volatile
        private var INSTANCE: SurveyRepository? = null

        fun getInstance(context: Context): SurveyRepository {
            return INSTANCE ?: synchronized(this) {
                val dbHelper = SurveyDatabaseHelper.getInstance(context.applicationContext)
                val instance = SurveyRepository(dbHelper)
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Inserts a new survey.
     *
     * @param survey The survey to insert.
     * @return The ID of the inserted survey.
     */
    suspend fun insertSurvey(survey: Survey): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        var surveyId: Long = -1
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE, survey.title)
                put(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION, survey.description)
            }
            surveyId = db.insert(SurveyDatabaseHelper.TABLE_SURVEYS, null, values)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        surveyId
    }

    /**
     * Updates an existing survey.
     *
     * @param survey The survey to update.
     */
    suspend fun updateSurvey(survey: Survey) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE, survey.title)
                put(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION, survey.description)
            }
            db.update(SurveyDatabaseHelper.TABLE_SURVEYS, values, "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?", arrayOf(survey.id.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Deletes a survey.
     *
     * @param surveyId The survey ID to delete.
     */
    suspend fun deleteSurvey(surveyId: Int) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(SurveyDatabaseHelper.TABLE_SURVEYS, "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?", arrayOf(surveyId.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Fetches all surveys.
     *
     * @return A list of all surveys.
     */
    suspend fun getAllSurveys(): List<Survey> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val surveys = mutableListOf<Survey>()
        val cursor = db.query(SurveyDatabaseHelper.TABLE_SURVEYS, null, null, null, null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_ID))
                val title = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE))
                val description = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION))
                surveys.add(Survey(id, title, description))
            }
        }
        surveys
    }

    /**
     * Inserts a new question.
     *
     * @param question The question to insert.
     * @return The ID of the inserted question.
     */
    suspend fun insertQuestion(question: Question): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        var questionId: Long = -1
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_SURVEY_ID_QUESTION, question.surveyId)
                put(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT, question.text)
            }
            questionId = db.insert(SurveyDatabaseHelper.TABLE_QUESTIONS, null, values)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        questionId
    }

    /**
     * Inserts multiple questions.
     *
     * @param questions The list of questions to insert.
     */
    suspend fun insertQuestions(questions: List<Question>) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            for (question in questions) {
                val values = ContentValues().apply {
                    put(SurveyDatabaseHelper.COLUMN_SURVEY_ID_QUESTION, question.surveyId)
                    put(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT, question.text)
                }
                db.insert(SurveyDatabaseHelper.TABLE_QUESTIONS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Updates an existing question.
     *
     * @param question The question to update.
     */
    suspend fun updateQuestion(question: Question) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT, question.text)
            }
            db.update(SurveyDatabaseHelper.TABLE_QUESTIONS, values, "${SurveyDatabaseHelper.COLUMN_QUESTION_ID} = ?", arrayOf(question.id.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Deletes a question.
     *
     * @param questionId The ID of the question to delete.
     */
    suspend fun deleteQuestion(questionId: Int) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(SurveyDatabaseHelper.TABLE_QUESTIONS, "${SurveyDatabaseHelper.COLUMN_QUESTION_ID} = ?", arrayOf(questionId.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Fetches all questions for a specific survey.
     *
     * @param surveyId The ID of the survey.
     * @return A list of questions for the specified survey.
     */
    suspend fun getQuestionsForSurvey(surveyId: Int): List<Question> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val questions = mutableListOf<Question>()
        val cursor = db.query(SurveyDatabaseHelper.TABLE_QUESTIONS, null, "${SurveyDatabaseHelper.COLUMN_SURVEY_ID_QUESTION} = ?", arrayOf(surveyId.toString()), null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_QUESTION_ID))
                val text = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT))
                questions.add(Question(id, surveyId, text))
            }
        }
        questions
    }

    /**
     * Inserts a new answer.
     *
     * @param answer The answer to insert.
     * @return The ID of the inserted answer.
     */
    suspend fun insertAnswer(answer: Answer): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        var answerId: Long = -1
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_QUESTION_ID_ANSWER, answer.questionId)
                put(SurveyDatabaseHelper.COLUMN_USER_ID_ANSWER, answer.userId)
                put(SurveyDatabaseHelper.COLUMN_ANSWER_VALUE, answer.answerValue)
            }
            answerId = db.insert(SurveyDatabaseHelper.TABLE_ANSWERS, null, values)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        answerId
    }

    /**
     * Fetches all answers for a specific question.
     *
     * @param questionId The ID of the question.
     * @return A list of answers for the specified question.
     */
    suspend fun getAnswersForQuestion(questionId: Int): List<Answer> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val answers = mutableListOf<Answer>()
        val cursor = db.query(SurveyDatabaseHelper.TABLE_ANSWERS, null, "${SurveyDatabaseHelper.COLUMN_QUESTION_ID_ANSWER} = ?", arrayOf(questionId.toString()), null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ANSWER_ID))
                val userId = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID_ANSWER))
                val answerValue = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ANSWER_VALUE))
                answers.add(Answer(id, questionId, userId, answerValue))
            }
        }
        answers
    }

    /**
     * Fetches a survey by ID.
     *
     * @param surveyId The ID of the survey.
     * @return The survey with the specified ID, or null if not found.
     */
    suspend fun getSurveyById(surveyId: Int): Survey? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        var survey: Survey? = null
        val cursor = db.query(SurveyDatabaseHelper.TABLE_SURVEYS, null, "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?", arrayOf(surveyId.toString()), null, null, null)
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_ID))
                val title = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE))
                val description = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION))
                survey = Survey(id, title, description)
            }
        }
        survey
    }
}
