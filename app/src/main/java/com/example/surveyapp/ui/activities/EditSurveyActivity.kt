package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabase
import com.example.surveyapp.models.Survey
import com.example.surveyapp.repositories.SurveyRepository
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for editing an existing survey.
 */
class EditSurveyActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.activities.SURVEY_ID"
    }

    private lateinit var surveyTitleEditText: EditText
    private lateinit var surveyDescriptionEditText: EditText
    private lateinit var updateSurveyButton: Button
    private val surveyViewModel: SurveyViewModel by viewModels {
        val surveyDao = SurveyDatabase.getDatabase(application).surveyDao()
        SurveyViewModelFactory(SurveyRepository(surveyDao))
    }

    private var surveyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_survey)

        // Initialize UI components
        surveyTitleEditText = findViewById(R.id.editTextSurveyTitle)
        surveyDescriptionEditText = findViewById(R.id.editTextSurveyDescription)
        updateSurveyButton = findViewById(R.id.buttonUpdateSurvey)

        // Retrieve the survey ID from the intent
        surveyId = intent.getIntExtra(EXTRA_SURVEY_ID, 0)
        if (surveyId != 0) {
            surveyViewModel.getSurveyById(surveyId) { survey ->
                if (survey != null) {
                    surveyTitleEditText.setText(survey.title)
                    surveyDescriptionEditText.setText(survey.description)
                } else {
                    Toast.makeText(this, "Survey not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Invalid survey ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Set click listener for updating the survey
        updateSurveyButton.setOnClickListener {
            val title = surveyTitleEditText.text.toString().trim()
            val description = surveyDescriptionEditText.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val updatedSurvey = Survey(id = surveyId, title = title, description = description)
                surveyViewModel.updateSurvey(updatedSurvey)
                Toast.makeText(this, "Survey Updated", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after updating the survey
            } else {
                Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
