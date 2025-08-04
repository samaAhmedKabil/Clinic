package com.example.clinic.ui.doctor.manageBookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.DisabledDate
import com.example.clinic.repos.DisabledDatesRepo

class DisabledDatesViewModel(private val repo: DisabledDatesRepo): ViewModel() {

    private val _disabledDates = MutableLiveData<List<DisabledDate>>()
    val disabledDates: LiveData<List<DisabledDate>> get() = _disabledDates

    private val _addStatus = MutableLiveData<Boolean?>()
    val addStatus: LiveData<Boolean?> get() = _addStatus

    fun loadDisabledDates() {
        repo.observeDisabledDates {
            _disabledDates.postValue(it)
        }
    }

    fun addDisabledDate(year: Int, month: Int, day: Int) {
        if (year < 2000 || month !in 1..12 || day !in 1..31) {
            _addStatus.value = false
            return
        }

        val date = DisabledDate(year, month, day)
        repo.addDisabledDate(date) { success ->
            _addStatus.postValue(success)
            if (success) loadDisabledDates()  // refresh
        }
    }

    fun deleteDisabledDate(date: DisabledDate) {
        repo.deleteDisabledDate(date)
        loadDisabledDates() // reload from Firebase so LiveData updates
    }

    fun clearStatus() {
        _addStatus.value = null
    }

    class Factory(private val repo: DisabledDatesRepo): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DisabledDatesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DisabledDatesViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}