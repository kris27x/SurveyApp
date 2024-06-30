package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyapp.models.User
import com.example.surveyapp.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun insert(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    fun getUser(username: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(username, password)
            callback(user)
        }
    }
}
