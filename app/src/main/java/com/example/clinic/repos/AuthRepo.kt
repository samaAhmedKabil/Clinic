package com.example.clinic.repos

import android.util.Log
import com.example.clinic.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthRepo {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun register(email: String, password: String, role: String, fName: String, lName: String, age: Int, phone: String, address: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // After successful authentication, add role and other details to the database
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                var user = User(userId, email, role, fName, lName, age, phone, address) // Assuming you have a User data class

                // Save user data to Firebase Realtime Database
                database.child("users").child(userId).setValue(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onResult(true) // Return success if the user is saved to the database
                        } else {
                            onResult(false) // Return failure if there was an issue saving the data
                        }
                    }
            }
            .addOnFailureListener {
                // If registration fails (due to invalid credentials or network issues)
                onResult(false)
            }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { // Login successful
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e("AuthRepo", "Login failed: ${exception.message}")
                onResult(false)
            }
    }
}