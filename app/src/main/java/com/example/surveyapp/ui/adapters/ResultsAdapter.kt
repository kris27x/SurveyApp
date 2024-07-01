package com.example.surveyapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question

/**
 * Adapter for displaying survey results.
 */
class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    private var questions: List<Question> = emptyList()
    private val answersMap: MutableMap<Int, Map<Int, Int>> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)
        return ResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, answersMap[question.id] ?: emptyMap())
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
     * @param answerCounts The map of answer values to their counts for the question.
     */
    fun updateAnswers(questionId: Int, answerCounts: Map<Int, Int>) {
        answersMap[questionId] = answerCounts
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
         * @param answerCounts The map of answer values to their counts.
         */
        fun bind(question: Question, answerCounts: Map<Int, Int>) {
            questionTextView.text = question.text
            val formattedAnswers = answerCounts.entries.joinToString(separator = "\n") { entry ->
                val likertText = when (entry.key) {
                    1 -> "Strongly Disagree"
                    2 -> "Disagree"
                    3 -> "Neutral"
                    4 -> "Agree"
                    5 -> "Strongly Agree"
                    else -> "No Response"
                }
                "$likertText: ${entry.value}"
            }
            answersTextView.text = formattedAnswers
        }
    }
}
