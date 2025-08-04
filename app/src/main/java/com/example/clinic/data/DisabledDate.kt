package com.example.clinic.data

data class DisabledDate(
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val id: String = ""  // push() key from Firebase
)
