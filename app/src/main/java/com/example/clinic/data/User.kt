package com.example.clinic.data

data class User(
    val userId: String,
    val email: String,
    val role: String,
    val fName: String,
    val lName: String,
    val age: Int,
    val phone: String,
    val address: String
)