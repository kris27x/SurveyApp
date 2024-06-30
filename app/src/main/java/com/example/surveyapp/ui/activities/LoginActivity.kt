package com.example.surveyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.repositories.UserRepository
import com.example.surveyapp.viewmodels.UserViewModel
import com.example.surveyapp.viewmodels.UserViewModelFactory

/**
 * Activity for user login.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var surveyDatabaseHelper: SurveyDatabaseHelper
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the database helper
        surveyDatabaseHelper = SurveyDatabaseHelper(this)

        // Initialize UI components
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)

        // Set click listener for login button
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.getUser(username, password) { user ->
                    if (user != null) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = if (user.isAdmin) {
                            Intent(this, AdminDashboardActivity::class.java)
                        } else {
                            Intent(this, UserDashboardActivity::class.java)
                        }
                        startActivity(intent)
                        finish() // Close the login activity
                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
