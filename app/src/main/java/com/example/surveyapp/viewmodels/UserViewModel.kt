package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyapp.models.User
import com.example.surveyapp.repositories.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for user-related UI components.
 *
 * @property repository The repository for accessing user data.
 */
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     * Inserts a new user.
     *
     * @param user The user to insert.
     */
    fun insert(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    /**
     * Fetches a user by username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param callback The callback to invoke with the fetched user.
     */
    fun getUser(username: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(username, password)
            callback(user)
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user The user to update.
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    /**
     * Deletes a user.
     *
     * @param user The user to delete.
     */
    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    /**
     * Fetches all users.
     *
     * @param callback The callback to invoke with the list of users.
     */
    fun getAllUsers(callback: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = repository.getAllUsers()
            callback(users)
        }
    }
}
