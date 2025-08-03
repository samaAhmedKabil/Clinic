package com.example.clinic.ui.doctor.commonQuestions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clinic.data.CommonQuestions
import com.example.clinic.repos.CommonQuestionsRepo
import kotlinx.coroutines.launch

class CommonQuestionsViewModel(private val repo: CommonQuestionsRepo) : ViewModel() {

    private val _questions = MutableLiveData<List<CommonQuestions>>()
    val questions: LiveData<List<CommonQuestions>> = _questions

    private val _uploadStatus = MutableLiveData<String>() // For success/error messages
    val uploadStatus: LiveData<String> = _uploadStatus

    private val _deleteStatus = MutableLiveData<String>()
    val deleteStatus: LiveData<String> = _deleteStatus

    private val _editStatus = MutableLiveData<String>()
    val editStatus: LiveData<String> = _editStatus

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            // Add a log here to see if this coroutine starts
            Log.d("ViewModel", "Starting to collect questions flow.")
            repo.getCommonQuestions().collect {
                // Add a log here to see if you receive data from the flow
                Log.d("ViewModel", "Received new data from flow: ${it.size} questions.")
                _questions.postValue(it)
            }
        }
    }

    fun uploadNewQuestion(questionText: String, answerText: String) {
        if (questionText.isBlank() || answerText.isBlank()) {
            _uploadStatus.postValue("Question and answer cannot be empty.")
            return
        }

        val newQuestion = CommonQuestions(question = questionText, answer = answerText)
        viewModelScope.launch {
            try {
                repo.uploadQuestion(newQuestion)
                _uploadStatus.postValue("Question uploaded successfully!")
            } catch (e: Exception) {
                _uploadStatus.postValue("Error uploading question: ${e.message}")
            }
        }
    }

    fun deleteQuestion(questionId: String) {
        viewModelScope.launch {
            try {
                repo.deleteQuestion(questionId)
                _deleteStatus.postValue("Deleted successfully!")
            } catch (e: Exception) {
                _deleteStatus.postValue("Error deleting: ${e.message}")
            }
        }
    }

    fun editQuestion(questionId: String, newQuestion: String, newAnswer: String) {
        if (newQuestion.isBlank() || newAnswer.isBlank()) {
            _editStatus.postValue("Question and answer cannot be empty.")
            return
        }

        viewModelScope.launch {
            try {
                val updated = CommonQuestions(id = questionId, question = newQuestion, answer = newAnswer)
                repo.updateQuestion(updated)
                _editStatus.postValue("Edited successfully!")
            } catch (e: Exception) {
                _editStatus.postValue("Error editing: ${e.message}")
            }
        }
    }


    // Factory for ViewModel (standard practice with constructor parameters)
    class CommonQuestionsViewModelFactory(private val repo: CommonQuestionsRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommonQuestionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommonQuestionsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}