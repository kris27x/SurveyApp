package com.example.surveyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.models.Survey
import com.example.surveyapp.ui.adapters.SurveyAdapter
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for admin dashboard. This activity allows admins to manage surveys.
 */
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var surveyRecyclerView: RecyclerView
    private lateinit var surveyAdapter: SurveyAdapter
    private lateinit var addSurveyButton: Button
    private lateinit var surveyDatabaseHelper: SurveyDatabaseHelper
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Initialize the database helper
        surveyDatabaseHelper = SurveyDatabaseHelper(this)

        // Initialize UI components
        surveyRecyclerView = findViewById(R.id.recyclerViewSurveys)
        addSurveyButton = findViewById(R.id.buttonAddSurvey)

        // Setup RecyclerView
        surveyRecyclerView.layoutManager = LinearLayoutManager(this)
        surveyAdapter = SurveyAdapter(
            { survey -> onSurveySelected(survey) },
            { survey -> onDeleteSurvey(survey) }
        )
        surveyRecyclerView.adapter = surveyAdapter

        // Observe the list of surveys
        surveyViewModel.getAllSurveys { surveys ->
            surveyAdapter.submitList(surveys)
        }

        // Set click listener for adding a new survey
        addSurveyButton.setOnClickListener {
            val intent = Intent(this, CreateSurveyActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Handles selection of a survey from the list.
     *
     * @param survey The selected survey.
     */
    private fun onSurveySelected(survey: Survey) {
        val intent = Intent(this, EditSurveyActivity::class.java).apply {
            putExtra(EditSurveyActivity.EXTRA_SURVEY_ID, survey.id)
        }
        startActivity(intent)
    }

    /**
     * Handles deletion of a survey.
     *
     * @param survey The survey to delete.
     */
    private fun onDeleteSurvey(survey: Survey) {
        surveyViewModel.deleteSurvey(survey.id)
        Toast.makeText(this, "Survey deleted", Toast.LENGTH_SHORT).show()
    }
}
