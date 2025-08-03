package com.example.clinic.data

data class DoctorBooking(
    val bookingId: String = "",
    val patientName: String = "",
    val patientPhone: String = "",
    val date: String = "",
    val slot: String = "",
    var finalState: String ?= null, // "attended", "not attended", or null
    var note: String? = null
)
