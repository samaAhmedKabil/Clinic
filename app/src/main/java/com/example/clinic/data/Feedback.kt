package com.example.clinic.data

data class Feedback(
    val id: String = "", // Firebase key
    val userId: String = "",
    val writerName: String = "",
    val text: String = "",
    val stars: Int = 0
)
