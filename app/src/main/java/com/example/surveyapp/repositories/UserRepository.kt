package com.example.surveyapp.repositories

import com.example.surveyapp.database.UserDao
import com.example.surveyapp.models.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User): Long {
        return userDao.insert(user)
    }

    suspend fun getUser(username: String, password: String): User? {
        return userDao.getUser(username, password)
    }
}
