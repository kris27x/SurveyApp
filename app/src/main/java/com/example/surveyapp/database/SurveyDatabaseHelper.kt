package com.example.surveyapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Survey
import com.example.surveyapp.models.User

/**
 * Helper class to manage database creation, connection, and version management for the Survey app.
 *
 * @param context The application context.
 */
class SurveyDatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_IS_ADMIN INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()

        val createSurveyTable = """
            CREATE TABLE $TABLE_SURVEYS (
                $COLUMN_SURVEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SURVEY_TITLE TEXT NOT NULL,
                $COLUMN_SURVEY_DESCRIPTION TEXT NOT NULL
            )
        """.trimIndent()

        val createQuestionTable = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SURVEY_ID_QUESTION INTEGER NOT NULL,
                $COLUMN_QUESTION_TEXT TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_SURVEY_ID_QUESTION) REFERENCES $TABLE_SURVEYS($COLUMN_SURVEY_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        val createAnswerTable = """
            CREATE TABLE $TABLE_ANSWERS (
                $COLUMN_ANSWER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_ID_ANSWER INTEGER NOT NULL,
                $COLUMN_USER_ID_ANSWER INTEGER NOT NULL,
                $COLUMN_ANSWER_VALUE INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_QUESTION_ID_ANSWER) REFERENCES $TABLE_QUESTIONS($COLUMN_QUESTION_ID) ON DELETE CASCADE,
                FOREIGN KEY ($COLUMN_USER_ID_ANSWER) REFERENCES $TABLE_USERS($COLUMN_USER_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createSurveyTable)
        db.execSQL(createQuestionTable)
        db.execSQL(createAnswerTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ANSWERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SURVEYS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
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
        const val COLUMN_SURVEY_ID_QUESTION = "surveyId"

        const val TABLE_ANSWERS = "answers"
        const val COLUMN_ANSWER_ID = "id"
        const val COLUMN_ANSWER_VALUE = "answerValue"
        const val COLUMN_QUESTION_ID_ANSWER = "questionId"
        const val COLUMN_USER_ID_ANSWER = "userId"

        @Volatile
        private var instance: SurveyDatabaseHelper? = null

        fun getInstance(context: Context): SurveyDatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: SurveyDatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }
}
