package com.example.surveyapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.surveyapp.models.User

/**
 * Data Access Object (DAO) for User entity.
 */
@Dao
interface UserDao {
    /**
     * Inserts a new user into the database.
     *
     * @param user The user to insert.
     * @return The ID of the inserted user.
     */
    @Insert
    suspend fun insert(user: User): Long

    /**
     * Fetches a user by username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user matching the given username and password, or null if no match is found.
     */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    /**
     * Fetches all admin users.
     *
     * @return A list of admin users.
     */
    @Query("SELECT * FROM users WHERE isAdmin = 1")
    suspend fun getAdminUsers(): List<User>
}
