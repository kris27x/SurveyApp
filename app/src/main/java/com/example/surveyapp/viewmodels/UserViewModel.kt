package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyapp.models.User
import com.example.surveyapp.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for user-related UI components.
 *
 * @property repository The repository for accessing user data.
 */
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     * Inserts a new user into the repository.
     *
     * @param user The user to insert.
     */
    fun insert(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(user)
            }
        }
    }

    /**
     * Fetches a user by username and password from the repository.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param callback The callback to invoke with the fetched user.
     */
    fun getUser(username: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                repository.getUser(username, password)
            }
            callback(user)
        }
    }

    /**
     * Updates an existing user in the repository.
     *
     * @param user The user to update.
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateUser(user)
            }
        }
    }

    /**
     * Deletes a user from the repository.
     *
     * @param user The user to delete.
     */
    fun deleteUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteUser(user)
            }
        }
    }

    /**
     * Fetches all admin users from the repository.
     *
     * @param callback The callback to invoke with the list of admin users.
     */
    fun getAdminUsers(callback: (List<User>) -> Unit) {
        viewModelScope.launch {
            val adminUsers = withContext(Dispatchers.IO) {
                repository.getAdminUsers()
            }
            callback(adminUsers)
        }
    }

    /**
     * Fetches all users from the repository.
     *
     * @param callback The callback to invoke with the list of users.
     */
    fun getAllUsers(callback: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = withContext(Dispatchers.IO) {
                repository.getAllUsers()
            }
            callback(users)
        }
    }
}
