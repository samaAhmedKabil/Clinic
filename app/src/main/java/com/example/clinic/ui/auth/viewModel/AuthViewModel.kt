package com.example.clinic.ui.auth.viewModel

import androidx.lifecycle.ViewModel
import com.example.clinic.repos.AuthRepo

class AuthViewModel: ViewModel() {
    private val authRepository = AuthRepo()

    fun register(email: String, password: String, role: String, fName: String, lName: String, age: Int, phone: String, address: String, onResult: (Boolean) -> Unit) {
        authRepository.register(email, password, role, fName, lName, age, phone, address, onResult)
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        authRepository.login(email, password, onResult)
    }
}