package com.example.surveyapp.repositories

import android.content.ContentValues
import android.content.Context
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.models.User

/**
 * Repository for managing user data operations.
 */
class UserRepository private constructor(private val dbHelper: SurveyDatabaseHelper) {

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(SurveyDatabaseHelper.getInstance(context))
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Inserts a new user.
     *
     * @param user The user to insert.
     * @return The ID of the inserted user.
     */
    fun insertUser(user: User): Long {
        val db = dbHelper.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_USERNAME, user.username)
                put(SurveyDatabaseHelper.COLUMN_PASSWORD, user.password)
                put(SurveyDatabaseHelper.COLUMN_IS_ADMIN, if (user.isAdmin) 1 else 0)
            }
            it.insert(SurveyDatabaseHelper.TABLE_USERS, null, values)
        }
    }

    /**
     * Fetches a user by username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user with the specified username and password, or null if not found.
     */
    fun getUser(username: String, password: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            SurveyDatabaseHelper.TABLE_USERS,
            null,
            "${SurveyDatabaseHelper.COLUMN_USERNAME} = ? AND ${SurveyDatabaseHelper.COLUMN_PASSWORD} = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
                val isAdmin = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_IS_ADMIN)) == 1
                User(id, username, password, isAdmin)
            } else {
                null
            }
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user The user to update.
     */
    fun updateUser(user: User): Int {
        val db = dbHelper.writableDatabase
        return db.use {
            val values = ContentValues().apply {
                put(SurveyDatabaseHelper.COLUMN_USERNAME, user.username)
                put(SurveyDatabaseHelper.COLUMN_PASSWORD, user.password)
                put(SurveyDatabaseHelper.COLUMN_IS_ADMIN, if (user.isAdmin) 1 else 0)
            }
            it.update(SurveyDatabaseHelper.TABLE_USERS, values, "${SurveyDatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(user.id.toString()))
        }
    }

    /**
     * Deletes a user.
     *
     * @param userId The ID of the user to delete.
     */
    fun deleteUser(userId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.use {
            it.delete(SurveyDatabaseHelper.TABLE_USERS, "${SurveyDatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(userId.toString()))
        }
    }

    /**
     * Fetches all admin users.
     *
     * @return A list of all admin users.
     */
    fun getAdminUsers(): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            SurveyDatabaseHelper.TABLE_USERS,
            null,
            "${SurveyDatabaseHelper.COLUMN_IS_ADMIN} = ?",
            arrayOf("1"),
            null,
            null,
            null
        )

        val adminUsers = mutableListOf<User>()
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
                val username = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USERNAME))
                val password = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_PASSWORD))
                adminUsers.add(User(id, username, password, true))
            }
        }
        return adminUsers
    }

    /**
     * Fetches all users.
     *
     * @return A list of all users.
     */
    fun getAllUsers(): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            SurveyDatabaseHelper.TABLE_USERS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val users = mutableListOf<User>()
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
                val username = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USERNAME))
                val password = it.getString(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_PASSWORD))
                val isAdmin = it.getInt(it.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_IS_ADMIN)) == 1
                users.add(User(id, username, password, isAdmin))
            }
        }
        return users
    }
}
