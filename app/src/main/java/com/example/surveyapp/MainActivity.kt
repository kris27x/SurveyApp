package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.ui.activities.LoginActivity

/**
 * Main activity and entry point of the app.
 * This activity serves as the launcher activity and immediately redirects
 * the user to the LoginActivity.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Launch the LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        // Close the MainActivity to prevent it from being accessible via the back button
        finish()
    }
}
