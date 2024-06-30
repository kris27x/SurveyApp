package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapp.repositories.UserRepository

/**
 * Factory for creating a UserViewModel with a repository.
 *
 * @property repository The repository for accessing user data.
 */
class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the given `Class`, with the repository injected.
     *
     * @param modelClass The `Class` of the ViewModel to create.
     * @return A newly created ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
