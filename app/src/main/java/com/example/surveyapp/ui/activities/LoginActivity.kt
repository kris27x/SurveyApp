package com.example.surveyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabase
import com.example.surveyapp.repositories.UserRepository
import com.example.surveyapp.viewmodels.UserViewModel
import com.example.surveyapp.viewmodels.UserViewModelFactory

/**
 * Activity for user login.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the ViewModel
        val userDao = SurveyDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Handle login button click
        loginButton.setOnClickListener {
            userViewModel.getUser(username.text.toString(), password.text.toString()) { user ->
                if (user != null) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = if (user.isAdmin) {
                        Intent(this, AdminDashboardActivity::class.java)
                    } else {
                        Intent(this, UserDashboardActivity::class.java)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
