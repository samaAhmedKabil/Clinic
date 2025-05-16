package com.example.clinic.data

data class User(
    val userId: String = "",
    val email: String = "",
    val role: String = "",
    val fname: String = "",
    val lname: String = "",
    val age: Int = 0,
    val phone: String = "",
    val address: String = ""
)