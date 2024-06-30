package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyapp.models.Survey
import com.example.surveyapp.models.Question
import com.example.surveyapp.models.Answer
import com.example.surveyapp.repositories.SurveyRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for survey-related UI components.
 *
 * @property repository The repository for accessing survey data.
 */
class SurveyViewModel(private val repository: SurveyRepository) : ViewModel() {

    /**
     * Inserts a new survey.
     *
     * @param survey The survey to insert.
     */
    fun insertSurvey(survey: Survey) {
        viewModelScope.launch {
            repository.insertSurvey(survey)
        }
    }

    /**
     * Updates an existing survey.
     *
     * @param survey The survey to update.
     */
    fun updateSurvey(survey: Survey) {
        viewModelScope.launch {
            repository.updateSurvey(survey)
        }
    }

    /**
     * Deletes a survey.
     *
     * @param survey The survey to delete.
     */
    fun deleteSurvey(survey: Survey) {
        viewModelScope.launch {
            repository.deleteSurvey(survey)
        }
    }

    /**
     * Fetches all surveys.
     *
     * @param callback The callback to invoke with the list of surveys.
     */
    fun getAllSurveys(callback: (List<Survey>) -> Unit) {
        viewModelScope.launch {
            val surveys = repository.getAllSurveys()
            callback(surveys)
        }
    }

    /**
     * Inserts a new question.
     *
     * @param question The question to insert.
     */
    fun insertQuestion(question: Question) {
        viewModelScope.launch {
            repository.insertQuestion(question)
        }
    }

    /**
     * Updates an existing question.
     *
     * @param question The question to update.
     */
    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            repository.updateQuestion(question)
        }
    }

    /**
     * Deletes a question.
     *
     * @param question The question to delete.
     */
    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            repository.deleteQuestion(question)
        }
    }

    /**
     * Fetches all questions for a specific survey.
     *
     * @param surveyId The ID of the survey.
     * @param callback The callback to invoke with the list of questions.
     */
    fun getQuestionsForSurvey(surveyId: Int, callback: (List<Question>) -> Unit) {
        viewModelScope.launch {
            val questions = repository.getQuestionsForSurvey(surveyId)
            callback(questions)
        }
    }

    /**
     * Inserts a new answer.
     *
     * @param answer The answer to insert.
     */
    fun insertAnswer(answer: Answer) {
        viewModelScope.launch {
            repository.insertAnswer(answer)
        }
    }

    /**
     * Fetches all answers for a specific question.
     *
     * @param questionId The ID of the question.
     * @param callback The callback to invoke with the list of answers.
     */
    fun getAnswersForQuestion(questionId: Int, callback: (List<Answer>) -> Unit) {
        viewModelScope.launch {
            val answers = repository.getAnswersForQuestion(questionId)
            callback(answers)
        }
    }
}
