package com.example.surveyapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Survey

/**
 * Helper class to manage database creation, connection, and version management for the Survey app.
 *
 * @param context The application context.
 */
class SurveyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create the users table
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_IS_ADMIN INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()

        // Create the surveys table
        val createSurveyTable = """
            CREATE TABLE $TABLE_SURVEYS (
                $COLUMN_SURVEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SURVEY_TITLE TEXT NOT NULL,
                $COLUMN_SURVEY_DESCRIPTION TEXT NOT NULL
            )
        """.trimIndent()

        // Create the questions table
        val createQuestionTable = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SURVEY_ID INTEGER NOT NULL,
                $COLUMN_QUESTION_TEXT TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_SURVEY_ID) REFERENCES $TABLE_SURVEYS($COLUMN_SURVEY_ID)
            )
        """.trimIndent()

        // Create the answers table
        val createAnswerTable = """
            CREATE TABLE $TABLE_ANSWERS (
                $COLUMN_ANSWER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_ID INTEGER NOT NULL,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_ANSWER_VALUE INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_QUESTION_ID) REFERENCES $TABLE_QUESTIONS($COLUMN_QUESTION_ID),
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createSurveyTable)
        db.execSQL(createQuestionTable)
        db.execSQL(createAnswerTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ANSWERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SURVEYS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        // Recreate tables
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "surveyapp.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_IS_ADMIN = "isAdmin"

        const val TABLE_SURVEYS = "surveys"
        const val COLUMN_SURVEY_ID = "id"
        const val COLUMN_SURVEY_TITLE = "title"
        const val COLUMN_SURVEY_DESCRIPTION = "description"

        const val TABLE_QUESTIONS = "questions"
        const val COLUMN_QUESTION_ID = "id"
        const val COLUMN_QUESTION_TEXT = "text"

        const val TABLE_ANSWERS = "answers"
        const val COLUMN_ANSWER_ID = "id"
        const val COLUMN_ANSWER_VALUE = "answerValue"
    }

    /**
     * Inserts a new survey into the database.
     *
     * @param survey The survey to insert.
     * @return The ID of the inserted survey.
     */
    fun insertSurvey(survey: Survey): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SURVEY_TITLE, survey.title)
            put(COLUMN_SURVEY_DESCRIPTION, survey.description)
        }
        val id = db.insert(TABLE_SURVEYS, null, values)
        db.close()
        return id
    }

    /**
     * Fetches all surveys from the database.
     *
     * @return A list of all surveys.
     */
    fun getAllSurveys(): List<Survey> {
        val surveys = mutableListOf<Survey>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_SURVEYS", null)
        if (cursor.moveToFirst()) {
            do {
                val survey = Survey(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SURVEY_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURVEY_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURVEY_DESCRIPTION))
                )
                surveys.add(survey)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return surveys
    }

    /**
     * Deletes a survey from the database.
     *
     * @param surveyId The ID of the survey to delete.
     * @return The number of rows affected.
     */
    fun deleteSurvey(surveyId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_SURVEYS, "$COLUMN_SURVEY_ID = ?", arrayOf(surveyId.toString()))
        db.close()
        return result
    }

    /**
     * Updates a question in the database.
     *
     * @param question The question to update.
     * @return The number of rows affected.
     */
    fun updateQuestion(question: Question): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT, question.text)
        }
        val result = db.update(TABLE_QUESTIONS, values, "$COLUMN_QUESTION_ID = ?", arrayOf(question.id.toString()))
        db.close()
        return result
    }

    /**
     * Deletes a question from the database.
     *
     * @param questionId The ID of the question to delete.
     * @return The number of rows affected.
     */
    fun deleteQuestion(questionId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_QUESTIONS, "$COLUMN_QUESTION_ID = ?", arrayOf(questionId.toString()))
        db.close()
        return result
    }

    /**
     * Fetches all questions for a specific survey from the database.
     *
     * @param surveyId The ID of the survey.
     * @return A list of questions for the survey.
     */
    fun getAllQuestions(surveyId: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_QUESTIONS WHERE $COLUMN_SURVEY_ID = ?", arrayOf(surveyId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val question = Question(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_ID)),
                    surveyId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SURVEY_ID)),
                    text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT))
                )
                questions.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return questions
    }
}
