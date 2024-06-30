package com.example.surveyapp.database

import androidx.room.*
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
     * Updates an existing user.
     *
     * @param user The user to update.
     */
    @Update
    suspend fun updateUser(user: User)

    /**
     * Deletes a user from the database.
     *
     * @param user The user to delete.
     */
    @Delete
    suspend fun deleteUser(user: User)

    /**
     * Fetches all admin users.
     *
     * @return A list of admin users.
     */
    @Query("SELECT * FROM users WHERE isAdmin = 1")
    suspend fun getAdminUsers(): List<User>

    /**
     * Fetches all users.
     *
     * @return A list of all users.
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
