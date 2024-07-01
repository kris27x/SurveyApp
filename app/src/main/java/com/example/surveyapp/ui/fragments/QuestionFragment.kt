package com.example.surveyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Question
import com.example.surveyapp.utils.LikertScale
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Fragment for displaying questions of a survey.
 */
class QuestionFragment : Fragment() {

    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var submitButton: Button
    private val surveyViewModel: SurveyViewModel by activityViewModels {
        SurveyViewModelFactory(requireContext().applicationContext)
    }

    private var surveyId: Int = 0
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        // Initialize UI components
        questionsRecyclerView = view.findViewById(R.id.recyclerViewQuestions)
        submitButton = view.findViewById(R.id.buttonSubmit)

        // Setup RecyclerView
        questionsRecyclerView.layoutManager = LinearLayoutManager(context)
        questionAdapter = QuestionAdapter(userId)
        questionsRecyclerView.adapter = questionAdapter

        // Retrieve survey ID from arguments
        arguments?.let {
            surveyId = it.getInt(EXTRA_SURVEY_ID)
            userId = it.getInt(EXTRA_USER_ID, 0)
        }

        // Fetch questions for the survey
        if (surveyId != 0) {
            surveyViewModel.getQuestionsForSurvey(surveyId) { questions ->
                questionAdapter.submitList(questions)
            }
        } else {
            Toast.makeText(context, "Invalid survey ID", Toast.LENGTH_SHORT).show()
        }

        // Handle submit button click
        submitButton.setOnClickListener {
            val answers = questionAdapter.getAnswers()
            if (answers.isNotEmpty()) {
                answers.forEach { answer ->
                    surveyViewModel.insertAnswer(answer)
                }
                Toast.makeText(context, "Answers Submitted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please answer all questions", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        const val EXTRA_SURVEY_ID = "com.example.surveyapp.ui.fragments.SURVEY_ID"
        const val EXTRA_USER_ID = "com.example.surveyapp.ui.fragments.USER_ID"
    }
}

// Adapter for displaying the list of questions
class QuestionAdapter(private val userId: Int) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var questions: List<Question> = emptyList()
    private val answersMap: MutableMap<Int, Int> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size

    fun submitList(questionList: List<Question>) {
        questions = questionList
        notifyDataSetChanged()
    }

    fun getAnswers(): List<Answer> {
        return questions.mapNotNull { question ->
            answersMap[question.id]?.let { answerValue ->
                Answer(questionId = question.id, userId = userId, answerValue = answerValue)
            }
        }
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.textViewQuestion)
        private val optionsRadioGroup: RadioGroup = itemView.findViewById(R.id.radioGroupOptions)

        fun bind(question: Question) {
            questionTextView.text = question.text

            // Set up radio buttons for Likert scale
            val options = listOf("Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree")
            optionsRadioGroup.removeAllViews() // Clear any previous radio buttons
            options.forEachIndexed { index, option ->
                val radioButton = RadioButton(itemView.context).apply {
                    text = option
                    id = index + 1
                }
                optionsRadioGroup.addView(radioButton)
            }

            // Handle option selection
            optionsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                answersMap[question.id] = checkedId
            }
        }
    }
}
