package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabaseHelper
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.utils.LikertScale
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for taking a survey.
 */
class TakeSurveyActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.activities.SURVEY_ID"
        const val EXTRA_USER_ID = "com.example.surveyapp.ui.activities.USER_ID"
        const val EXTRA_IS_ADMIN = "com.example.surveyapp.ui.activities.IS_ADMIN"
    }

    private lateinit var surveyDatabaseHelper: SurveyDatabaseHelper
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }

    private var surveyId: Int = 0
    private var userId: Int = 0
    private var isAdmin: Boolean = false
    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private var currentQuestionIndex = 0
    private var questions: List<Question> = emptyList()
    private var answers: MutableMap<Int, Int> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_survey)

        // Retrieve the survey ID, user ID, and admin status from the intent
        surveyId = intent.getIntExtra(EXTRA_SURVEY_ID, 0)
        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false)

        // Initialize the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Take Survey"

        // Initialize the database helper
        surveyDatabaseHelper = SurveyDatabaseHelper(this)

        // Initialize UI components
        questionTextView = findViewById(R.id.textViewQuestion)
        optionsRadioGroup = findViewById(R.id.radioGroupOptions)
        submitButton = findViewById(R.id.buttonSubmit)

        if (surveyId != 0) {
            surveyViewModel.getQuestionsForSurvey(surveyId) { fetchedQuestions ->
                questions = fetchedQuestions
                if (questions.isNotEmpty()) {
                    displayQuestion()
                } else {
                    Toast.makeText(this, "No questions available for this survey", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Invalid survey ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Set click listener for submit button
        submitButton.setOnClickListener {
            submitAnswer()
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                displayQuestion()
            } else {
                saveAnswers()
                Toast.makeText(this, "Survey Completed", Toast.LENGTH_SHORT).show()
                finish()
            }
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

    /**
     * Displays the current question and its options.
     */
    private fun displayQuestion() {
        val question = questions[currentQuestionIndex]
        questionTextView.text = question.text
        optionsRadioGroup.removeAllViews()

        val options = listOf("Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree")
        options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this).apply {
                text = option
                id = index + 1
            }
            optionsRadioGroup.addView(radioButton)
        }

        val selectedOption = answers[question.id]
        if (selectedOption != null) {
            optionsRadioGroup.check(selectedOption)
        }
    }

    /**
     * Submits the current answer.
     */
    private fun submitAnswer() {
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        if (selectedOptionId != -1) {
            val questionId = questions[currentQuestionIndex].id
            answers[questionId] = selectedOptionId
        }
    }

    /**
     * Saves all the answers to the database.
     */
    private fun saveAnswers() {
        answers.forEach { (questionId, selectedOptionId) ->
            val answerValue = when (selectedOptionId) {
                1 -> LikertScale.STRONGLY_DISAGREE
                2 -> LikertScale.DISAGREE
                3 -> LikertScale.NEUTRAL
                4 -> LikertScale.AGREE
                5 -> LikertScale.STRONGLY_AGREE
                else -> LikertScale.NEUTRAL
            }

            val answer = Answer(
                questionId = questionId,
                userId = userId, // Pass the logged-in user ID
                answerValue = answerValue
            )

            surveyViewModel.insertAnswer(answer)
        }
    }
}
