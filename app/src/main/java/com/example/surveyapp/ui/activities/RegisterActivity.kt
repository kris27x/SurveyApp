package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.surveyapp.R
import com.example.surveyapp.models.User
import com.example.surveyapp.viewmodels.UserViewModel
import com.example.surveyapp.viewmodels.UserViewModelFactory

/**
 * Activity for user registration.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI components
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        registerButton = findViewById(R.id.registerButton)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register"

        // Handle back button in toolbar
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Set click listener for register button
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val newUser = User(
                    username = username,
                    password = password,
                    isAdmin = false // Default to false for regular users
                )
                userViewModel.insert(newUser)
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after registration
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
