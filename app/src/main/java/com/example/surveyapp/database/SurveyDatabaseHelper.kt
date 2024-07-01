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
        const val COLUMN_SURVEY_ID_QUESTION = "surveyId" // Renamed to avoid conflict

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

    // CRUD methods for Survey
    fun insertSurvey(survey: Survey): Long {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_SURVEY_TITLE, survey.title)
                put(COLUMN_SURVEY_DESCRIPTION, survey.description)
            }
            it.insert(TABLE_SURVEYS, null, values)
        }
    }

    fun getAllSurveys(): List<Survey> {
        val surveys = mutableListOf<Survey>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_SURVEYS", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val survey = Survey(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_SURVEY_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COLUMN_SURVEY_TITLE)),
                        description = it.getString(it.getColumnIndexOrThrow(COLUMN_SURVEY_DESCRIPTION))
                    )
                    surveys.add(survey)
                } while (it.moveToNext())
            }
        }
        return surveys
    }

    fun getSurveyById(surveyId: Int): Survey? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SURVEYS,
            null,
            "$COLUMN_SURVEY_ID = ?",
            arrayOf(surveyId.toString()),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                Survey(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_SURVEY_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_SURVEY_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_SURVEY_DESCRIPTION))
                )
            } else {
                null
            }
        }
    }

    fun deleteSurvey(surveyId: Int): Int {
        val db = this.writableDatabase
        return db.use {
            it.delete(TABLE_SURVEYS, "$COLUMN_SURVEY_ID = ?", arrayOf(surveyId.toString()))
        }
    }

    fun updateSurvey(survey: Survey): Int {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_SURVEY_TITLE, survey.title)
                put(COLUMN_SURVEY_DESCRIPTION, survey.description)
            }
            it.update(TABLE_SURVEYS, values, "$COLUMN_SURVEY_ID = ?", arrayOf(survey.id.toString()))
        }
    }

    // CRUD methods for Question
    fun insertQuestion(question: Question): Long {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_SURVEY_ID_QUESTION, question.surveyId)
                put(COLUMN_QUESTION_TEXT, question.text)
            }
            it.insert(TABLE_QUESTIONS, null, values)
        }
    }

    fun insertQuestions(questions: List<Question>) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (question in questions) {
                val values = ContentValues().apply {
                    put(COLUMN_SURVEY_ID_QUESTION, question.surveyId)
                    put(COLUMN_QUESTION_TEXT, question.text)
                }
                db.insert(TABLE_QUESTIONS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getQuestionsForSurvey(surveyId: Int): List<Question> {
        val questions = mutableListOf<Question>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_QUESTIONS WHERE $COLUMN_SURVEY_ID_QUESTION = ?", arrayOf(surveyId.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val question = Question(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUESTION_ID)),
                        surveyId = it.getInt(it.getColumnIndexOrThrow(COLUMN_SURVEY_ID_QUESTION)),
                        text = it.getString(it.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT))
                    )
                    questions.add(question)
                } while (it.moveToNext())
            }
        }
        return questions
    }

    fun updateQuestion(question: Question): Int {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_QUESTION_TEXT, question.text)
            }
            it.update(TABLE_QUESTIONS, values, "$COLUMN_QUESTION_ID = ?", arrayOf(question.id.toString()))
        }
    }

    fun deleteQuestion(questionId: Int): Int {
        val db = this.writableDatabase
        return db.use {
            it.delete(TABLE_QUESTIONS, "$COLUMN_QUESTION_ID = ?", arrayOf(questionId.toString()))
        }
    }

    // CRUD methods for Answer
    fun insertAnswer(answer: Answer): Long {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_QUESTION_ID_ANSWER, answer.questionId)
                put(COLUMN_USER_ID_ANSWER, answer.userId)
                put(COLUMN_ANSWER_VALUE, answer.answerValue)
            }
            it.insert(TABLE_ANSWERS, null, values)
        }
    }

    fun insertAnswers(answers: List<Answer>) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (answer in answers) {
                val values = ContentValues().apply {
                    put(COLUMN_QUESTION_ID_ANSWER, answer.questionId)
                    put(COLUMN_USER_ID_ANSWER, answer.userId)
                    put(COLUMN_ANSWER_VALUE, answer.answerValue)
                }
                db.insert(TABLE_ANSWERS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getAnswersForQuestion(questionId: Int): List<Answer> {
        val answers = mutableListOf<Answer>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_ANSWERS WHERE $COLUMN_QUESTION_ID_ANSWER = ?", arrayOf(questionId.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val answer = Answer(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ANSWER_ID)),
                        questionId = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUESTION_ID_ANSWER)),
                        userId = it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_ID_ANSWER)),
                        answerValue = it.getInt(it.getColumnIndexOrThrow(COLUMN_ANSWER_VALUE))
                    )
                    answers.add(answer)
                } while (it.moveToNext())
            }
        }
        return answers
    }

    // CRUD methods for User
    fun insertUser(user: User): Long {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_IS_ADMIN, if (user.isAdmin) 1 else 0)
            }
            it.insert(TABLE_USERS, null, values)
        }
    }

    fun getUser(username: String, password: String): User? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", arrayOf(username, password))
        return cursor.use {
            if (it.moveToFirst()) {
                User(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    username = it.getString(it.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    password = it.getString(it.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    isAdmin = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1
                )
            } else {
                null
            }
        }
    }

    // Additional CRUD methods for User
    fun updateUser(user: User): Int {
        val db = this.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_IS_ADMIN, if (user.isAdmin) 1 else 0)
            }
            it.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))
        }
    }

    fun deleteUser(userId: Int): Int {
        val db = this.writableDatabase
        return db.use {
            it.delete(TABLE_USERS, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        }
    }
}
