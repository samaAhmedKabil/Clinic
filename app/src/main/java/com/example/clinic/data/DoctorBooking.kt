package com.example.clinic.data

data class DoctorBooking(
    val bookingId: String = "",
    val patientName: String = "",
    val patientPhone: String = "",
    val date: String = "",
    val slot: String = ""
)