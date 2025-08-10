package com.example.clinic.ui.patient.forYou.babyKicks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.BabyKicks
import com.example.clinic.repos.BabyKicksRepo

class BabyKicksViewModel(private val repo: BabyKicksRepo) : ViewModel() {

    private val _kicks = MutableLiveData<BabyKicks>()
    val kicks: LiveData<BabyKicks> get() = _kicks

    init {
        repo.getKicks {
            _kicks.value = it
        }
    }

    fun incrementKick() {
        val newCount = (_kicks.value?.kicksCount ?: 0) + 1
        repo.updateKicks(newCount)
    }

    fun resetKicks() {
        repo.resetKicks()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repo: BabyKicksRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BabyKicksViewModel(repo) as T
        }
    }
}