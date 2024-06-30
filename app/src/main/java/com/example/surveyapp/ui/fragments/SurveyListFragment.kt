package com.example.surveyapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Survey
import com.example.surveyapp.ui.activities.TakeSurveyActivity
import com.example.surveyapp.viewmodels.SurveyViewModel
import com.example.surveyapp.viewmodels.SurveyViewModelFactory

/**
 * Fragment for displaying a list of surveys.
 */
class SurveyListFragment : Fragment() {

    private lateinit var surveyRecyclerView: RecyclerView
    private lateinit var surveyAdapter: SurveyAdapter
    private val surveyViewModel: SurveyViewModel by activityViewModels {
        SurveyViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_survey_list, container, false)

        // Initialize UI components
        surveyRecyclerView = view.findViewById(R.id.recyclerViewSurveys)
        surveyRecyclerView.layoutManager = LinearLayoutManager(context)
        surveyAdapter = SurveyAdapter { survey -> onSurveySelected(survey) }
        surveyRecyclerView.adapter = surveyAdapter

        // Observe the list of surveys
        surveyViewModel.getAllSurveys { surveys ->
            surveyAdapter.submitList(surveys)
        }

        return view
    }

    /**
     * Handles selection of a survey from the list.
     *
     * @param survey The selected survey.
     */
    private fun onSurveySelected(survey: Survey) {
        val intent = Intent(activity, TakeSurveyActivity::class.java).apply {
            putExtra(TakeSurveyActivity.EXTRA_SURVEY_ID, survey.id)
        }
        startActivity(intent)
    }
}

// Adapter for displaying the list of surveys
class SurveyAdapter(private val onSurveyClick: (Survey) -> Unit) :
    RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    private var surveys: List<Survey> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_survey, parent, false)
        return SurveyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        val survey = surveys[position]
        holder.bind(survey, onSurveyClick)
    }

    override fun getItemCount(): Int = surveys.size

    fun submitList(surveyList: List<Survey>) {
        surveys = surveyList
        notifyDataSetChanged()
    }

    class SurveyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val surveyTitle: TextView = itemView.findViewById(R.id.textViewSurveyTitle)
        private val surveyDescription: TextView = itemView.findViewById(R.id.textViewSurveyDescription)

        fun bind(survey: Survey, onSurveyClick: (Survey) -> Unit) {
            surveyTitle.text = survey.title
            surveyDescription.text = survey.description
            itemView.setOnClickListener { onSurveyClick(survey) }
        }
    }
}
