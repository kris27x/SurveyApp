package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapp.repositories.SurveyRepository

/**
 * Factory for creating a SurveyViewModel with a repository.
 *
 * @property repository The repository for accessing survey data.
 */
class SurveyViewModelFactory(private val repository: SurveyRepository) : ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the given `Class`, with the repository injected.
     *
     * @param modelClass The `Class` of the ViewModel to create.
     * @return A newly created ViewModel.
     * @throws IllegalArgumentException if the model class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurveyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
