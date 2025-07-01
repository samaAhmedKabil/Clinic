package com.example.clinic.data

data class CommonQuestions(
    val id: String = "", // Firebase will generate this, but useful for local tracking
    val question: String = "",
    val answer: String = ""
)