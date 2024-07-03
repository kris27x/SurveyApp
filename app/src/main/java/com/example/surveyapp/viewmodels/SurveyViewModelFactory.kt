package com.example.surveyapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapp.repositories.SurveyRepository

/**
 * Factory for creating a SurveyViewModel with a repository.
 *
 * @property context The context for accessing resources and creating the repository.
 */
class SurveyViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the given `Class`, with the repository injected.
     *
     * @param modelClass The `Class` of the ViewModel to create.
     * @return A newly created ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
            val repository = SurveyRepository.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return SurveyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
