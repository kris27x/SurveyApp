package com.example.surveyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyapp.models.Answer
import com.example.surveyapp.models.Survey
import com.example.surveyapp.models.Question
import com.example.surveyapp.repositories.SurveyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.IO) {
                repository.insertSurvey(survey)
            }
        }
    }

    /**
     * Updates an existing survey.
     *
     * @param survey The survey to update.
     */
    fun updateSurvey(survey: Survey) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateSurvey(survey)
            }
        }
    }

    /**
     * Deletes a survey.
     *
     * @param survey The survey to delete.
     */
    fun deleteSurvey(surveyId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteSurvey(surveyId)
            }
        }
    }

    /**
     * Fetches all surveys.
     *
     * @param callback The callback to invoke with the list of surveys.
     */
    fun getAllSurveys(callback: (List<Survey>) -> Unit) {
        viewModelScope.launch {
            val surveys = withContext(Dispatchers.IO) {
                repository.getAllSurveys()
            }
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
            withContext(Dispatchers.IO) {
                repository.insertQuestion(question)
            }
        }
    }

    /**
     * Updates an existing question.
     *
     * @param question The question to update.
     */
    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateQuestion(question)
            }
        }
    }

    /**
     * Deletes a question.
     *
     * @param question The question to delete.
     */
    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteQuestion(question)
            }
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
            val questions = withContext(Dispatchers.IO) {
                repository.getQuestionsForSurvey(surveyId)
            }
            callback(questions)
        }
    }

    /**
     * Fetches a survey by ID.
     *
     * @param surveyId The ID of the survey.
     * @param callback The callback to invoke with the fetched survey.
     */
    fun getSurveyById(surveyId: Int, callback: (Survey?) -> Unit) {
        viewModelScope.launch {
            val survey = withContext(Dispatchers.IO) {
                repository.getSurveyById(surveyId)
            }
            callback(survey)
        }
    }

    /**
     * Inserts a new answer.
     *
     * @param answer The answer to insert.
     */
    fun insertAnswer(answer: Answer) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertAnswer(answer)
            }
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
            val answers = withContext(Dispatchers.IO) {
                repository.getAnswersForQuestion(questionId)
            }
            callback(answers)
        }
    }
}
