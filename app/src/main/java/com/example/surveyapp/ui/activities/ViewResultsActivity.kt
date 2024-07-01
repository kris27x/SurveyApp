package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.ui.adapters.ResultsAdapter
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for viewing survey results and statistics.
 */
class ViewResultsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.activities.SURVEY_ID"
        const val EXTRA_USER_ID = "com.example.surveyapp.ui.activities.USER_ID"
        const val EXTRA_IS_ADMIN = "com.example.surveyapp.ui.activities.IS_ADMIN"
    }

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }
    private var surveyId: Int = 0
    private var userId: Int = 0
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_results)

        // Initialize the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "View Results"

        // Initialize UI components
        resultsRecyclerView = findViewById(R.id.recyclerViewResults)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsAdapter = ResultsAdapter()
        resultsRecyclerView.adapter = resultsAdapter

        // Retrieve the survey ID, user ID, and admin status from the intent
        surveyId = intent.getIntExtra(EXTRA_SURVEY_ID, 0)
        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false)

        if (surveyId != 0) {
            surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
                resultsAdapter.submitList(questions)
                questions.forEach { question ->
                    surveyViewModel.getAnswersForQuestion(question.id) { answers ->
                        val answerCounts = answers.groupBy { it.answerValue }.mapValues { it.value.size }
                        resultsAdapter.updateAnswers(question.id, answerCounts)
                    }
                }
            }
        } else {
            finish() // Close activity if survey ID is invalid
        }
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
