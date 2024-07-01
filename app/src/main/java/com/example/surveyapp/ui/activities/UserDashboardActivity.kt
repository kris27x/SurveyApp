package com.example.surveyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Survey
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for user dashboard.
 */
class UserDashboardActivity : AppCompatActivity() {

    private lateinit var surveyRecyclerView: RecyclerView
    private lateinit var surveyAdapter: SurveyAdapter
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

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
            { surveyId -> onDeleteSurvey(surveyId) }
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
        }
        startActivity(intent)
    }

    /**
     * Handles deletion of a survey.
     *
     * @param surveyId The ID of the survey to delete.
     */
    private fun onDeleteSurvey(surveyId: Int) {
        surveyViewModel.deleteSurvey(surveyId)
        surveyViewModel.getAllSurveys { surveys ->
            surveyAdapter.submitList(surveys)
        }
        Toast.makeText(this, "Survey deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

/**
 * Adapter for displaying the list of surveys.
 *
 * @property onSurveyClick Callback to handle survey item click events.
 * @property onDeleteClick Callback to handle survey delete button click events.
 */
class SurveyAdapter(
    private val onSurveyClick: (Survey) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    private var surveys: List<Survey> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_survey, parent, false)
        return SurveyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        val survey = surveys[position]
        holder.bind(survey, onSurveyClick, onDeleteClick)
    }

    override fun getItemCount(): Int = surveys.size

    /**
     * Submits a new list of surveys to the adapter.
     *
     * @param surveyList The new list of surveys.
     */
    fun submitList(surveyList: List<Survey>) {
        surveys = surveyList
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class for displaying survey items.
     *
     * @property itemView The view of the survey item.
     */
    class SurveyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val surveyTitle: TextView = itemView.findViewById(R.id.textViewSurveyTitle)
        private val surveyDescription: TextView = itemView.findViewById(R.id.textViewSurveyDescription)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDeleteSurvey)

        /**
         * Binds the survey data to the views.
         *
         * @param survey The survey data to bind.
         * @param onSurveyClick Callback to handle survey item click events.
         * @param onDeleteClick Callback to handle survey delete button click events.
         */
        fun bind(survey: Survey, onSurveyClick: (Survey) -> Unit, onDeleteClick: (Int) -> Unit) {
            surveyTitle.text = survey.title
            surveyDescription.text = survey.description
            itemView.setOnClickListener { onSurveyClick(survey) }

            deleteButton.setOnClickListener {
                onDeleteClick(survey.id)
            }
        }
    }
}
