package com.example.surveyapp.models

/**
 * Data class representing an Admin entity in the application.
 *
 * @property id The unique ID of the admin.
 * @property username The username of the admin.
 * @property password The password of the admin.
 */
data class Admin(
    val id: Int = 0,
    val username: String,
    val password: String
)
