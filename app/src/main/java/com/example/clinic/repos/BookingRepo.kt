package com.example.clinic.repos

import com.example.clinic.data.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingRepo {
    private val auth = FirebaseAuth.getInstance() // Firebase Authentication instance
    private val database = FirebaseDatabase.getInstance().reference // Firebase Realtime Database reference

    fun bookAppointment(booking: Booking, onResult: (Boolean) -> Unit) {
        // Get the currently authenticated user's UID
        val userId = auth.currentUser?.uid

        if (userId == null) {
            // If the user is not authenticated, return failure
            onResult(false)
            return
        }

        // Save booking under the user's UID in the "bookings" node
        database.child("bookings").push().setValue(booking)
            .addOnSuccessListener {
                // On success, return true
                onResult(true)
            }
            .addOnFailureListener {
                // On failure, return false
                onResult(false)
            }
    }

    fun getBookedSlotsForDate(date: String, onResult: (List<String>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("bookings")

        val bookedSlots = mutableListOf<String>()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                        val bookingDate = userSnapshot.child("date").getValue(String::class.java)
                        val timeSlot = userSnapshot.child("timeSlot").getValue(String::class.java)
                        if (bookingDate == date && timeSlot != null) {
                            bookedSlots.add(timeSlot)
                        }
                }
                onResult(bookedSlots)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList()) // Return empty if error
            }
        })
    }

    fun isUserAlreadyBooked(date: String, userId: String, onResult: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("bookings")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (bookingSnapshot in snapshot.children) {
                    val bookingDate = bookingSnapshot.child("date").getValue(String::class.java)
                    val bookingUserId = bookingSnapshot.child("patientId").getValue(String::class.java)

                    if (bookingDate == date && bookingUserId == userId) {
                        onResult(true)
                        return
                    }
                }
                onResult(false)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(false) // Treat as not booked on error
            }
        })
    }

}