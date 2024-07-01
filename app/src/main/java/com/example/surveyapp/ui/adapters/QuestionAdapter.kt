package com.example.surveyapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Question

/**
 * Adapter for displaying and managing questions in a survey.
 *
 * @param onUpdateQuestion A function to handle updating a question.
 * @param onDeleteQuestion A function to handle deleting a question.
 */
class QuestionAdapter(
    private val onUpdateQuestion: (Question) -> Unit,
    private val onDeleteQuestion: (Question) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var questions: List<Question> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, onUpdateQuestion, onDeleteQuestion)
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
     * ViewHolder class for displaying individual questions.
     *
     * @param itemView The view of the individual question item.
     */
    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.textViewQuestionText1)
        private val questionEditText: EditText = itemView.findViewById(R.id.editTextQuestionText2)
        private val updateButton: Button = itemView.findViewById(R.id.buttonUpdateQuestion)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDeleteQuestion)

        /**
         * Binds a question to the ViewHolder.
         *
         * @param question The question to bind.
         * @param onUpdateQuestion A function to handle updating the question.
         * @param onDeleteQuestion A function to handle deleting the question.
         */
        fun bind(
            question: Question,
            onUpdateQuestion: (Question) -> Unit,
            onDeleteQuestion: (Question) -> Unit
        ) {
            questionTextView.text = question.text
            questionEditText.setText(question.text)

            updateButton.setOnClickListener {
                val updatedText = questionEditText.text.toString().trim()
                if (updatedText.isNotEmpty() && updatedText != question.text) {
                    val updatedQuestion = question.copy(text = updatedText)
                    onUpdateQuestion(updatedQuestion)
                }
            }

            deleteButton.setOnClickListener {
                onDeleteQuestion(question)
            }
        }
    }
}
