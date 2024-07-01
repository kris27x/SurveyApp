package com.example.surveyapp.repositories

import android.content.ContentValues
import android.database.Cursor
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Survey

/**
 * Repository for managing survey data operations.
 *
 * @property dbHelper The database helper for accessing survey data.
 */
class SurveyRepository(private val dbHelper: SurveyDatabaseHelper) {

    /**
     * Inserts a new survey.
     *
     * @param survey The survey to insert.
     * @return The ID of the inserted survey.
     */
    fun insertSurvey(survey: Survey): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE, survey.title)
            put(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION, survey.description)
        }
        return db.insert(SurveyDatabaseHelper.TABLE_SURVEYS, null, values)
    }

    /**
     * Updates an existing survey.
     *
     * @param survey The survey to update.
     */
    fun updateSurvey(survey: Survey) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE, survey.title)
            put(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION, survey.description)
        }
        db.update(SurveyDatabaseHelper.TABLE_SURVEYS, values, "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?", arrayOf(survey.id.toString()))
    }

    /**
     * Deletes a survey.
     *
     * @param surveyId The survey ID to delete.
     */
    fun deleteSurvey(surveyId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(SurveyDatabaseHelper.TABLE_SURVEYS, "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?", arrayOf(surveyId.toString()))
    }

    /**
     * Fetches all surveys.
     *
     * @return A list of all surveys.
     */
    fun getAllSurveys(): List<Survey> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            SurveyDatabaseHelper.TABLE_SURVEYS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val surveys = mutableListOf<Survey>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION))
            surveys.add(Survey(id, title, description))
        }
        cursor.close()
        return surveys
    }

    /**
     * Inserts a new question.
     *
     * @param question The question to insert.
     * @return The ID of the inserted question.
     */
    fun insertQuestion(question: Question): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_SURVEY_ID, question.surveyId)
            put(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT, question.text)
        }
        return db.insert(SurveyDatabaseHelper.TABLE_QUESTIONS, null, values)
    }

    /**
     * Inserts multiple questions.
     *
     * @param questions The list of questions to insert.
     */
    fun insertQuestions(questions: List<Question>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            for (question in questions) {
                val values = ContentValues().apply {
                    put(SurveyDatabaseHelper.COLUMN_SURVEY_ID, question.surveyId)
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
    fun updateQuestion(question: Question) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT, question.text)
        }
        db.update(SurveyDatabaseHelper.TABLE_QUESTIONS, values, "${SurveyDatabaseHelper.COLUMN_QUESTION_ID} = ?", arrayOf(question.id.toString()))
    }

    /**
     * Deletes a question.
     *
     * @param question The question to delete.
     */
    fun deleteQuestion(question: Question) {
        val db = dbHelper.writableDatabase
        db.delete(SurveyDatabaseHelper.TABLE_QUESTIONS, "${SurveyDatabaseHelper.COLUMN_QUESTION_ID} = ?", arrayOf(question.id.toString()))
    }

    /**
     * Fetches all questions for a specific survey.
     *
     * @param surveyId The ID of the survey.
     * @return A list of questions for the specified survey.
     */
    fun getQuestionsForSurvey(surveyId: Int): List<Question> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            SurveyDatabaseHelper.TABLE_QUESTIONS,
            null,
            "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?",
            arrayOf(surveyId.toString()),
            null,
            null,
            null
        )

        val questions = mutableListOf<Question>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_QUESTION_ID))
            val text = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_QUESTION_TEXT))
            questions.add(Question(id, surveyId, text))
        }
        cursor.close()
        return questions
    }

    /**
     * Inserts a new answer.
     *
     * @param answer The answer to insert.
     * @return The ID of the inserted answer.
     */
    fun insertAnswer(answer: Answer): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_QUESTION_ID, answer.questionId)
            put(SurveyDatabaseHelper.COLUMN_USER_ID, answer.userId)
            put(SurveyDatabaseHelper.COLUMN_ANSWER_VALUE, answer.answerValue)
        }
        return db.insert(SurveyDatabaseHelper.TABLE_ANSWERS, null, values)
    }

    /**
     * Fetches all answers for a specific question.
     *
     * @param questionId The ID of the question.
     * @return A list of answers for the specified question.
     */
    fun getAnswersForQuestion(questionId: Int): List<Answer> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            SurveyDatabaseHelper.TABLE_ANSWERS,
            null,
            "${SurveyDatabaseHelper.COLUMN_QUESTION_ID} = ?",
            arrayOf(questionId.toString()),
            null,
            null,
            null
        )

        val answers = mutableListOf<Answer>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ANSWER_ID))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
            val answerValue = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ANSWER_VALUE))
            answers.add(Answer(id, questionId, userId, answerValue))
        }
        cursor.close()
        return answers
    }

    /**
     * Fetches a survey by ID.
     *
     * @param surveyId The ID of the survey.
     * @return The survey with the specified ID, or null if not found.
     */
    fun getSurveyById(surveyId: Int): Survey? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            SurveyDatabaseHelper.TABLE_SURVEYS,
            null,
            "${SurveyDatabaseHelper.COLUMN_SURVEY_ID} = ?",
            arrayOf(surveyId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_SURVEY_DESCRIPTION))
            cursor.close()
            Survey(id, title, description)
        } else {
            cursor.close()
            null
        }
    }
}
