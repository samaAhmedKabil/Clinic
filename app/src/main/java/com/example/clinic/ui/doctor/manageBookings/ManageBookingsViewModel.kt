package com.example.clinic.ui.doctor.manageBookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.Booking
import com.example.clinic.repos.BookingRepo
import com.example.clinic.ui.patient.booking.BookingViewModel
import com.google.firebase.auth.FirebaseAuth

class ManageBookingsViewModel(private val repo: BookingRepo):ViewModel() {
    private val _bookingStatus = MutableLiveData<Boolean?>()
    val bookingStatus: MutableLiveData<Boolean?> get() = _bookingStatus

    private val _bookingError = MutableLiveData<String?>()
    val bookingError: MutableLiveData<String?> get() = _bookingError

    private var selectedDate: String? = null
    private var selectedSlot: String? = null

    // Set selected date and slot
    fun setSelectedDate(date: String) {
        selectedDate = date
    }

    fun setSelectedSlot(slot: String) {
        selectedSlot = slot
    }

    // Confirm booking and add patient ID
    fun confirmBooking() {
        val date = selectedDate
        val slot = selectedSlot
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            _bookingError.value = "User not authenticated!"
            return
        }

        if (date.isNullOrEmpty() || slot.isNullOrEmpty()) {
            _bookingError.value = "Please select a date and slot!"
            return
        }

        val booking = Booking(
            id = "",
            patientId = userId,
            date = date,
            timeSlot = slot
        )

        repo.bookAppointment(booking) { success ->
            if (success) {
                _bookingStatus.postValue(true)
            } else {
                _bookingError.postValue("Error: Booking Failed")
            }
        }
    }


    fun clearStatus() {
        _bookingStatus.value = null
        _bookingError.value = null
    }

    private val _availableSlots = MutableLiveData<List<String>>()
    val availableSlots: LiveData<List<String>> get() = _availableSlots

    private val allSlots = listOf(
        "6:00 PM", "6:10 PM", "6:20 PM", "6:30 PM", "6:40 PM", "6:50 PM",
        "7:00 PM", "7:10 PM", "7:20 PM", "7:30 PM", "7:40 PM", "7:50 PM",
        "8:00 PM", "8:10 PM", "8:20 PM", "8:30 PM", "8:40 PM", "8:50 PM",
        "9:00 PM", "9:10 PM", "9:20 PM", "9:30 PM", "9:40 PM", "9:50 PM", "10:00 PM"
    )

    private val _bookedSlots = MutableLiveData<List<String>>()
    val bookedSlots: LiveData<List<String>> get() = _bookedSlots

    fun loadAvailableSlots(date: String) {
        repo.getBookedSlotsForDate(date) { bookedSlots ->
            _bookedSlots.postValue(bookedSlots)  // save booked slots
            val filteredSlots = allSlots.filter { it !in bookedSlots }
            _availableSlots.postValue(filteredSlots)
        }
    }

    class ManageBookingsViewModelFactory(private val repo: BookingRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ManageBookingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ManageBookingsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}