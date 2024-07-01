package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.R
import com.example.surveyapp.models.Survey
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for creating a new survey.
 */
class CreateSurveyActivity : AppCompatActivity() {

    private lateinit var surveyTitleEditText: EditText
    private lateinit var surveyDescriptionEditText: EditText
    private lateinit var createSurveyButton: Button
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_survey)

        // Initialize UI components
        surveyTitleEditText = findViewById(R.id.editTextSurveyTitle)
        surveyDescriptionEditText = findViewById(R.id.editTextSurveyDescription)
        createSurveyButton = findViewById(R.id.buttonCreateSurvey)

        // Set click listener for creating a new survey
        createSurveyButton.setOnClickListener {
            val title = surveyTitleEditText.text.toString().trim()
            val description = surveyDescriptionEditText.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val newSurvey = Survey(title = title, description = description)
                surveyViewModel.insertSurvey(newSurvey)
                Toast.makeText(this, "Survey Created", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after creating the survey
            } else {
                Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
