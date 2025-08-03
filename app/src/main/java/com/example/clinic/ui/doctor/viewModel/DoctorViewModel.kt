package com.example.clinic.ui.doctor.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.DoctorBooking
import com.example.clinic.data.Patient
import com.example.clinic.repos.PatientRepo

class DoctorViewModel(private val repo: PatientRepo) : ViewModel() {
    private val _patientsList = MutableLiveData<List<Patient>>()
    val patientsList: LiveData<List<Patient>> get() = _patientsList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Fetch all patients
    fun fetchAllPatients() {
        repo.getAllPatients(
            onResult = { patients ->
                _patientsList.value = patients
            },
            onFailure = { error ->
                _errorMessage.value = "Error fetching data: $error"
            }
        )
    }

    private val _patient = MutableLiveData<Patient?>()
    val patient: MutableLiveData<Patient?> get() = _patient

    fun getPatientById(patientId: String) {
        repo.getPatientById(patientId,
            onSuccess = { patient ->
                _patient.postValue(patient)
            },
            onFailure = {
                _patient.postValue(null)
            }
        )
    }

    private val _bookingsList = MutableLiveData<List<DoctorBooking>>()
    val bookingsList: LiveData<List<DoctorBooking>> get() = _bookingsList


    fun updateBookingFinalState(bookingId: String, finalState: String, onResult: (Boolean) -> Unit) {
        repo.updateBookingFinalState(bookingId, finalState, onResult)
    }

    fun fetchBookingsByDate(date: String) {
        repo.getBookingsByDate(
            date = date,
            onResult = { bookings ->
                _bookingsList.value = bookings
            },
            onFailure = { error ->
                _errorMessage.value = "Error fetching bookings: $error"
            }
        )
    }

    fun updateBookingNote(bookingId: String, note: String, onResult: (Boolean) -> Unit) {
        repo.updateBookingNote(bookingId, note, onResult)
    }

    class Factory(private val repo: PatientRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DoctorViewModel::class.java)) {
                return DoctorViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}