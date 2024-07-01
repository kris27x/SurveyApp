package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Survey
import com.example.surveyapp.ui.adapters.QuestionAdapter
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for editing an existing survey.
 */
class EditSurveyActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.activities.SURVEY_ID"
        const val EXTRA_USER_ID = "com.example.surveyapp.ui.activities.USER_ID"
        const val EXTRA_IS_ADMIN = "com.example.surveyapp.ui.activities.IS_ADMIN"
    }

    private lateinit var surveyTitleEditText: EditText
    private lateinit var surveyDescriptionEditText: EditText
    private lateinit var updateSurveyButton: Button
    private lateinit var addQuestionButton: Button
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }

    private var surveyId: Int = 0
    private var userId: Int = 0
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_survey)

        // Retrieve the survey ID, user ID, and admin status from the intent
        surveyId = intent.getIntExtra(EXTRA_SURVEY_ID, 0)
        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false)

        // Initialize UI components
        surveyTitleEditText = findViewById(R.id.editTextSurveyTitle)
        surveyDescriptionEditText = findViewById(R.id.editTextSurveyDescription)
        updateSurveyButton = findViewById(R.id.buttonUpdateSurvey)
        addQuestionButton = findViewById(R.id.buttonAddQuestion)
        questionRecyclerView = findViewById(R.id.recyclerViewQuestions)

        // Setup RecyclerView
        questionRecyclerView.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(
            { question -> onUpdateQuestion(question) },
            { question -> onDeleteQuestion(question) }
        )
        questionRecyclerView.adapter = questionAdapter

        // Fetch survey data
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

            // Fetch questions for the survey
            surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
                questionAdapter.submitList(questions)
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

        // Set click listener for adding a new question
        addQuestionButton.setOnClickListener {
            val newQuestion = Question(surveyId = surveyId, text = "New Question")
            surveyViewModel.insertQuestion(newQuestion) {
                if (it != null) {
                    Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show()
                    // Refresh the questions list
                    surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
                        questionAdapter.submitList(questions)
                    }
                } else {
                    Toast.makeText(this, "Error adding question", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Handles updating a question.
     *
     * @param question The question to update.
     */
    private fun onUpdateQuestion(question: Question) {
        surveyViewModel.updateQuestion(question)
        Toast.makeText(this, "Question Updated", Toast.LENGTH_SHORT).show()
        // Refresh the questions list
        surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
            questionAdapter.submitList(questions)
        }
    }

    /**
     * Handles deleting a question.
     *
     * @param question The question to delete.
     */
    private fun onDeleteQuestion(question: Question) {
        surveyViewModel.deleteQuestion(question)
        Toast.makeText(this, "Question Deleted", Toast.LENGTH_SHORT).show()
        // Refresh the questions list
        surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
            questionAdapter.submitList(questions)
        }
    }
}
