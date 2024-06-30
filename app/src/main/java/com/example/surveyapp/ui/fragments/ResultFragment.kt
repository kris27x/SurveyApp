package com.example.surveyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.database.SurveyDatabase
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.repositories.SurveyRepository
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Fragment for displaying survey results.
 */
class ResultFragment : Fragment() {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private val surveyViewModel: SurveyViewModel by activityViewModels {
        val surveyDao = SurveyDatabase.getDatabase(requireContext().applicationContext).surveyDao()
        SurveyViewModelFactory(SurveyRepository(surveyDao))
    }

    private var surveyId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        // Initialize UI components
        resultsRecyclerView = view.findViewById(R.id.recyclerViewResults)
        resultsRecyclerView.layoutManager = LinearLayoutManager(context)
        resultsAdapter = ResultsAdapter()
        resultsRecyclerView.adapter = resultsAdapter

        // Retrieve survey ID from arguments
        arguments?.let {
            surveyId = it.getInt(EXTRA_SURVEY_ID)
        }

        // Fetch questions and their answers for the survey
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
            Toast.makeText(context, "Invalid survey ID", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.fragments.SURVEY_ID"
    }
}

// Adapter for displaying survey results
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

    fun submitList(questionList: List<Question>) {
        questions = questionList
        notifyDataSetChanged()
    }

    fun updateAnswers(questionId: Int, answers: List<Answer>) {
        answersMap[questionId] = answers
        notifyItemChanged(questions.indexOfFirst { it.id == questionId })
    }

    class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.textViewQuestion)
        private val answersTextView: TextView = itemView.findViewById(R.id.textViewAnswers)

        fun bind(question: Question, answers: List<Answer>) {
            questionTextView.text = question.text
            answersTextView.text = answers.joinToString(separator = "\n") { "User ${it.userId}: ${it.answerValue}" }
        }
    }
}
