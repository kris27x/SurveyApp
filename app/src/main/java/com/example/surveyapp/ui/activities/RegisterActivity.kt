package com.example.surveyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
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
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var adminCheckBox: CheckBox
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI components
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirmPassword)
        registerButton = findViewById(R.id.registerButton)
        adminCheckBox = findViewById(R.id.adminCheckBox)
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
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            val isAdmin = adminCheckBox.isChecked

            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    userViewModel.getUser(username, password) { existingUser ->
                        if (existingUser == null) {
                            val newUser = User(
                                username = username,
                                password = password,
                                isAdmin = isAdmin
                            )
                            userViewModel.insert(newUser)
                            Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()

                            val intent = if (isAdmin) {
                                Intent(this, AdminDashboardActivity::class.java)
                            } else {
                                Intent(this, UserDashboardActivity::class.java)
                            }
                            intent.putExtra("USER_ID", newUser.id)
                            intent.putExtra("IS_ADMIN", isAdmin)
                            startActivity(intent)
                            finish() // Close the activity after registration
                        } else {
                            Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
