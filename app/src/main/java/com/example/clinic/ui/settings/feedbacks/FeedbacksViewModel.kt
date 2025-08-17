package com.example.clinic.ui.settings.feedbacks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.Feedback
import com.example.clinic.repos.FeedbackRepo

class FeedbacksViewModel(private val repo: FeedbackRepo) : ViewModel() {

    private val _feedbacks = MutableLiveData<List<Feedback>>()
    val feedbacks: LiveData<List<Feedback>> = _feedbacks

    fun loadFeedbacks(itemId: String) {
        repo.getFeedbacks(itemId) {
            _feedbacks.postValue(it)
        }
    }

    fun uploadFeedback(itemId: String, feedback: Feedback, onComplete: (Boolean) -> Unit) {
        repo.uploadFeedback(itemId, feedback, onComplete)
    }

    fun deleteFeedback(itemId: String, feedbackId: String, onComplete: (Boolean) -> Unit) {
        repo.deleteFeedback(itemId, feedbackId, onComplete)
    }

    fun hasUserFeedback(itemId: String, userId: String, onResult: (Boolean) -> Unit) {
        repo.hasUserFeedback(itemId, userId, onResult)
    }

    // Factory for ViewModel with a constructor parameter (isDoctor)
    class FeedbackViewModelFactory(private val repo: FeedbackRepo) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedbacksViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedbacksViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}