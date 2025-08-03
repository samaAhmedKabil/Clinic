package com.example.clinic.ui.doctor.manageBookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.Booking
import com.example.clinic.repos.BookingRepo
import com.example.clinic.ui.patient.booking.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageBookingsViewModel(private val repo: BookingRepo):ViewModel() {
    private val _bookingStatus = MutableLiveData<Boolean?>()
    val bookingStatus: MutableLiveData<Boolean?> get() = _bookingStatus

    private val _bookingError = MutableLiveData<String?>()
    val bookingError: MutableLiveData<String?> get() = _bookingError

    private var selectedDate: String? = null
    internal var selectedSlot: String? = null

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
            timeSlot = slot,
            isDeletable = true,
            finalState = "",
            note = bookingNote.orEmpty()
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

    private var bookingNote: String? = null

    fun setBookingNote(note: String) {
        bookingNote = note
    }

    fun addNoteToBooking(note: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val date = selectedDate
        val slot = selectedSlot

        if (userId == null || date.isNullOrEmpty() || slot.isNullOrEmpty()) {
            _bookingError.value = "Missing data to add note"
            return
        }

        // Find the user's booking on that date and slot to get bookingId
        val bookingsRef = FirebaseDatabase.getInstance().getReference("bookings")

        bookingsRef.orderByChild("patientId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var bookingId: String? = null
                    for (bookingSnapshot in snapshot.children) {
                        val bookingDate = bookingSnapshot.child("date").getValue(String::class.java)
                        val timeSlot = bookingSnapshot.child("timeSlot").getValue(String::class.java)
                        if (bookingDate == date && timeSlot == slot) {
                            bookingId = bookingSnapshot.key
                            break
                        }
                    }
                    if (bookingId != null) {
                        val noteRef = bookingsRef.child(bookingId).child("note")
                        noteRef.setValue(note)
                            .addOnSuccessListener {
                                _bookingError.postValue("Note added successfully") // or use _bookingStatus
                            }
                            .addOnFailureListener {
                                _bookingError.postValue("Failed to add note")
                            }
                    } else {
                        _bookingError.postValue("Booking not found to add note")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _bookingError.postValue("Failed to query booking: ${error.message}")
                }
            })
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