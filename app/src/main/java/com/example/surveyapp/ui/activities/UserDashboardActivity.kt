package com.example.surveyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Survey
import com.example.surveyapp.ui.adapters.SurveyAdapter
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for user dashboard.
 */
class UserDashboardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "com.example.surveyapp.ui.activities.USER_ID"
        const val EXTRA_IS_ADMIN = "com.example.surveyapp.ui.activities.IS_ADMIN"
    }

    private lateinit var surveyRecyclerView: RecyclerView
    private lateinit var surveyAdapter: SurveyAdapter
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }
    private var userId: Int = 0
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        // Retrieve user ID and admin status from intent extras
        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false)

        // Setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Dashboard"

        // Initialize UI components
        surveyRecyclerView = findViewById(R.id.recyclerViewSurveys)

        // Setup RecyclerView
        surveyRecyclerView.layoutManager = LinearLayoutManager(this)
        surveyAdapter = SurveyAdapter(
            { survey -> onSurveySelected(survey) },
            { survey -> onDeleteSurvey(survey) },
            { survey -> onEditSurvey(survey) },
            isAdmin
        )
        surveyRecyclerView.adapter = surveyAdapter

        // Observe the list of surveys
        surveyViewModel.getAllSurveys { surveys ->
            if (surveys.isNotEmpty()) {
                surveyAdapter.submitList(surveys)
            } else {
                Toast.makeText(this, "No surveys available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Handles selection of a survey from the list.
     *
     * @param survey The selected survey.
     */
    private fun onSurveySelected(survey: Survey) {
        val intent = Intent(this, TakeSurveyActivity::class.java).apply {
            putExtra(TakeSurveyActivity.EXTRA_SURVEY_ID, survey.id)
            putExtra(TakeSurveyActivity.EXTRA_USER_ID, userId)
            putExtra(TakeSurveyActivity.EXTRA_IS_ADMIN, isAdmin)
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
        surveyViewModel.getAllSurveys { surveys ->
            surveyAdapter.submitList(surveys)
        }
        Toast.makeText(this, "Survey deleted", Toast.LENGTH_SHORT).show()
    }

    /**
     * Handles editing of a survey.
     *
     * @param survey The survey to edit.
     */
    private fun onEditSurvey(survey: Survey) {
        val intent = Intent(this, EditSurveyActivity::class.java).apply {
            putExtra(EditSurveyActivity.EXTRA_SURVEY_ID, survey.id)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
