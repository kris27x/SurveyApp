package com.example.surveyapp.repositories

import com.example.surveyapp.database.UserDao
import com.example.surveyapp.models.User

/**
 * Repository for managing user data operations.
 *
 * @property userDao The DAO for accessing user data.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Inserts a new user.
     *
     * @param user The user to insert.
     * @return The ID of the inserted user.
     */
    suspend fun insert(user: User): Long {
        return userDao.insert(user)
    }

    /**
     * Fetches a user by username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user matching the given username and password, or null if no match is found.
     */
    suspend fun getUser(username: String, password: String): User? {
        return userDao.getUser(username, password)
    }
}
