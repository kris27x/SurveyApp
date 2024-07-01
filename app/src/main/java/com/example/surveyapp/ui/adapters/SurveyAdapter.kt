package com.example.surveyapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import com.example.surveyapp.models.Survey

/**
 * Adapter for displaying the list of surveys.
 *
 * @property onSurveyClick Callback to handle survey item click events.
 * @property onDeleteClick Callback to handle delete button click events.
 * @property onEditClick Callback to handle edit button click events.
 * @property isAdmin Determines if the user is an admin.
 */
class SurveyAdapter(
    private val onSurveyClick: (Survey) -> Unit,
    private val onDeleteClick: (Survey) -> Unit,
    private val onEditClick: (Survey) -> Unit,
    private val isAdmin: Boolean
) : RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    private var surveys: List<Survey> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_survey, parent, false)
        return SurveyViewHolder(view, isAdmin, onDeleteClick, onEditClick)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        val survey = surveys[position]
        holder.bind(survey, onSurveyClick)
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
     * @property isAdmin Determines if the user is an admin.
     * @property onDeleteClick Callback to handle survey delete button click events.
     * @property onEditClick Callback to handle survey edit button click events.
     */
    class SurveyViewHolder(
        itemView: View,
        private val isAdmin: Boolean,
        private val onDeleteClick: (Survey) -> Unit,
        private val onEditClick: (Survey) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val surveyTitle: TextView = itemView.findViewById(R.id.textViewSurveyTitle)
        private val surveyDescription: TextView = itemView.findViewById(R.id.textViewSurveyDescription)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDeleteSurvey)
        private val editButton: Button = itemView.findViewById(R.id.buttonEditSurvey)

        /**
         * Binds the survey data to the views.
         *
         * @param survey The survey data to bind.
         * @param onSurveyClick Callback to handle survey item click events.
         */
        fun bind(survey: Survey, onSurveyClick: (Survey) -> Unit) {
            surveyTitle.text = survey.title
            surveyDescription.text = survey.description
            itemView.setOnClickListener { onSurveyClick(survey) }

            if (isAdmin) {
                deleteButton.visibility = View.VISIBLE
                editButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener { onDeleteClick(survey) }
                editButton.setOnClickListener { onEditClick(survey) }
            } else {
                deleteButton.visibility = View.GONE
                editButton.visibility = View.GONE
            }
        }
    }
}
