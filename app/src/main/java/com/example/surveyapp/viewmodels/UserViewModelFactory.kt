package com.example.surveyapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapp.repositories.UserRepository

/**
 * Factory for creating a UserViewModel with a repository.
 *
 * @property context The context for accessing resources and creating the repository.
 */
class UserViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the given `Class`, with the repository injected.
     *
     * @param modelClass The `Class` of the ViewModel to create.
     * @return A newly created ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            val repository = UserRepository.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
