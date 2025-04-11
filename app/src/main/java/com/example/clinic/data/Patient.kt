package com.example.clinic.data

import java.io.Serializable

data class Patient(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val age: Int,
    val email: String = "",
    val address: String = "",
) : Serializable