package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabase
import com.example.surveyapp.models.User
import com.example.surveyapp.repositories.UserRepository
import com.example.surveyapp.viewmodels.UserViewModel
import com.example.surveyapp.viewmodels.UserViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val userDao = SurveyDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val user = User(
                id = 0, // Auto-generated
                username = username.text.toString(),
                password = password.text.toString(),
                isAdmin = false // Default to false for regular users
            )
            userViewModel.insert(user)
            Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
        }
    }
}
