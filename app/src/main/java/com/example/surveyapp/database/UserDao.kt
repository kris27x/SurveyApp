package com.example.surveyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.surveyapp.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE isAdmin = 1")
    suspend fun getAdminUsers(): List<User>
}
