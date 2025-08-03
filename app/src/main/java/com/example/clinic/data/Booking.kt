package com.example.clinic.data

data class Booking(
    val id: String = "",
    val patientId: String = "",
    val date: String = "",
    val timeSlot: String = "",
    var isDeletable: Boolean = true,
    var finalState: String = "", // New field to store 'attended' or 'not attended'
    var note: String? = null
)