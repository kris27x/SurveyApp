package com.example.surveyapp.models

/**
 * Data class representing a User entity in the database.
 *
 * @property id The unique ID of the user.
 * @property username The username of the user.
 * @property password The password of the user.
 * @property isAdmin Flag indicating whether the user is an admin.
 */
data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val isAdmin: Boolean = false
)
