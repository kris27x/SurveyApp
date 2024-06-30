package com.example.surveyapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Survey

class SurveyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

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
                $COLUMN_SURVEY_ID INTEGER NOT NULL,
                $COLUMN_QUESTION_TEXT TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_SURVEY_ID) REFERENCES $TABLE_SURVEYS($COLUMN_SURVEY_ID)
            )
        """.trimIndent()

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

        const val TABLE_ANSWERS = "answers"
        const val COLUMN_ANSWER_ID = "id"
        const val COLUMN_ANSWER_VALUE = "answerValue"
    }

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

    fun deleteSurvey(surveyId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_SURVEYS, "$COLUMN_SURVEY_ID = ?", arrayOf(surveyId.toString()))
        db.close()
        return result
    }

    fun updateQuestion(question: Question): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT, question.text)
        }
        val result = db.update(TABLE_QUESTIONS, values, "$COLUMN_QUESTION_ID = ?", arrayOf(question.id.toString()))
        db.close()
        return result
    }

    fun deleteQuestion(questionId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_QUESTIONS, "$COLUMN_QUESTION_ID = ?", arrayOf(questionId.toString()))
        db.close()
        return result
    }

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
