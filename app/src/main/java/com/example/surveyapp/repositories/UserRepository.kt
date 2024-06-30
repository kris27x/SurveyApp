package com.example.surveyapp.repositories

import android.content.ContentValues
import android.content.Context
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.models.User

/**
 * Repository for managing user data operations.
 *
 * @property context The context for accessing resources and creating the database helper.
 */
class UserRepository(context: Context) {
    private val dbHelper = SurveyDatabaseHelper(context)

    /**
     * Inserts a new user.
     *
     * @param user The user to insert.
     * @return The ID of the inserted user.
     */
    fun insert(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_USERNAME, user.username)
            put(SurveyDatabaseHelper.COLUMN_PASSWORD, user.password)
            put(SurveyDatabaseHelper.COLUMN_IS_ADMIN, if (user.isAdmin) 1 else 0)
        }
        return db.insert(SurveyDatabaseHelper.TABLE_USERS, null, values)
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

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
            val isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_IS_ADMIN)) == 1
            val user = User(id, username, password, isAdmin)
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user The user to update.
     */
    fun updateUser(user: User) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SurveyDatabaseHelper.COLUMN_USERNAME, user.username)
            put(SurveyDatabaseHelper.COLUMN_PASSWORD, user.password)
            put(SurveyDatabaseHelper.COLUMN_IS_ADMIN, if (user.isAdmin) 1 else 0)
        }
        db.update(SurveyDatabaseHelper.TABLE_USERS, values, "${SurveyDatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(user.id.toString()))
    }

    /**
     * Deletes a user.
     *
     * @param user The user to delete.
     */
    fun deleteUser(user: User) {
        val db = dbHelper.writableDatabase
        db.delete(SurveyDatabaseHelper.TABLE_USERS, "${SurveyDatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(user.id.toString()))
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
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USERNAME))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_PASSWORD))
            adminUsers.add(User(id, username, password, true))
        }
        cursor.close()
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
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USER_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_USERNAME))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_PASSWORD))
            val isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_IS_ADMIN)) == 1
            users.add(User(id, username, password, isAdmin))
        }
        cursor.close()
        return users
    }
}
