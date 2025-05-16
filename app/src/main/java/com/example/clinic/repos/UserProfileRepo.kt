package com.example.clinic.repos

import com.example.clinic.data.User
import com.google.firebase.database.FirebaseDatabase

class UserProfileRepo(private val database: FirebaseDatabase) {

    fun getUserProfile(userId: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        database.getReference("users").child(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.getValue(User::class.java)?.let { user ->
                    onSuccess(user)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}