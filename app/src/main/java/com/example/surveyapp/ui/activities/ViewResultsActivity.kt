package com.example.surveyapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Activity for viewing survey results.
 */
class ViewResultsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.activities.SURVEY_ID"
    }

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(this)
    }
    private var surveyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_results)

        // Initialize the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize UI components
        resultsRecyclerView = findViewById(R.id.recyclerViewResults)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsAdapter = ResultsAdapter()
        resultsRecyclerView.adapter = resultsAdapter

        // Retrieve the survey ID from the intent
        surveyId = intent.getIntExtra(EXTRA_SURVEY_ID, 0)
        if (surveyId != 0) {
            surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
                resultsAdapter.submitList(questions)
                questions.forEach { question ->
                    surveyViewModel.getAnswersForQuestion(question.id) { answers ->
                        resultsAdapter.updateAnswers(question.id, answers)
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

/**
 * Adapter for displaying survey results.
 */
class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    private var questions: List<Question> = emptyList()
    private val answersMap: MutableMap<Int, List<Answer>> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)
        return ResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, answersMap[question.id] ?: emptyList())
    }

    override fun getItemCount(): Int = questions.size

    /**
     * Submits a new list of questions to the adapter.
     *
     * @param questionList The new list of questions.
     */
    fun submitList(questionList: List<Question>) {
        questions = questionList
        notifyDataSetChanged()
    }

    /**
     * Updates the answers for a specific question.
     *
     * @param questionId The ID of the question.
     * @param answers The list of answers for the question.
     */
    fun updateAnswers(questionId: Int, answers: List<Answer>) {
        answersMap[questionId] = answers
        notifyItemChanged(questions.indexOfFirst { it.id == questionId })
    }

    /**
     * ViewHolder class for displaying survey results.
     */
    class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.textViewQuestion)
        private val answersTextView: TextView = itemView.findViewById(R.id.textViewAnswers)

        /**
         * Binds the question and its answers to the views.
         *
         * @param question The question to bind.
         * @param answers The list of answers to bind.
         */
        fun bind(question: Question, answers: List<Answer>) {
            questionTextView.text = question.text
            answersTextView.text = answers.joinToString(separator = "\n") { "User ${it.userId}: ${it.answerValue}" }
        }
    }
}
