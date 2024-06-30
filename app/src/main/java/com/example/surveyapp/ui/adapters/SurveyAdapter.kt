package com.example.surveyapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Survey
import com.example.surveyapp.database.SurveyDatabaseHelper

/**
 * Adapter for displaying the list of surveys.
 *
 * @property onSurveyClick Callback to handle survey item click events.
 * @property onDeleteClick Callback to handle delete button click events.
 */
class SurveyAdapter(
    private val onSurveyClick: (Survey) -> Unit,
    private val onDeleteClick: (Survey) -> Unit
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
         * @param onDeleteClick Callback to handle delete button click events.
         */
        fun bind(
            survey: Survey,
            onSurveyClick: (Survey) -> Unit,
            onDeleteClick: (Survey) -> Unit
        ) {
            surveyTitle.text = survey.title
            surveyDescription.text = survey.description
            itemView.setOnClickListener { onSurveyClick(survey) }
            deleteButton.setOnClickListener { onDeleteClick(survey) }
        }
    }
}
