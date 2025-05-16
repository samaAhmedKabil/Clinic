package com.example.clinic.ui.myProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.User
import com.example.clinic.repos.UserProfileRepo

class MyProfileViewModel(private val repository: UserProfileRepo): ViewModel() {
    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> get() = _userProfile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadUserProfile(userId: String) {
        repository.getUserProfile(
            userId,
            onSuccess = { user -> _userProfile.value = user },
            onFailure = { e -> _error.value = e.message }
        )
    }

    class MyProfileViewModelFactory(private val repository: UserProfileRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyProfileViewModel::class.java)) {
                return MyProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}