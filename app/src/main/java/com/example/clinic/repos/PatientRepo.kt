package com.example.clinic.repos

import com.example.clinic.utils.ConstData
import com.example.clinic.data.Patient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientRepo {
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun getAllPatients(onResult: (List<Patient>) -> Unit, onFailure: (String) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val patients = mutableListOf<Patient>()
                for (userSnapshot in snapshot.children) {
                    val role = userSnapshot.child("role").getValue(String::class.java) ?: ""
                    if (role != ConstData.PATIENT_TYPE) continue

                    val id = userSnapshot.key ?: continue
                    val fName = userSnapshot.child("fname").getValue(String::class.java) ?: ""
                    val lName = userSnapshot.child("lname").getValue(String::class.java) ?: ""
                    val phone = userSnapshot.child("phone").getValue(String::class.java) ?: ""
                    val age = userSnapshot.child("age").getValue(Int::class.java) ?: 0
                    val email = userSnapshot.child("email").getValue(String::class.java) ?: ""
                    val address = userSnapshot.child("address").getValue(String::class.java) ?: ""

                    val patient = Patient(id = id, name = "$fName $lName", phone = phone, age = age, email = email, address = address)
                    patients.add(patient)
                }
                onResult(patients)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }
        })
    }

    // Fetch a single patient by ID
    fun getPatientById(patientId: String, onSuccess: (Patient?) -> Unit, onFailure: (String) -> Unit) {
        database.child(patientId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val role = snapshot.child("role").getValue(String::class.java) ?: ""
                    if (role != ConstData.PATIENT_TYPE) {
                        onSuccess(null)
                        return@addOnSuccessListener
                    }

                    val fName = snapshot.child("fname").getValue(String::class.java) ?: ""
                    val lName = snapshot.child("lname").getValue(String::class.java) ?: ""
                    val phone = snapshot.child("phone").getValue(String::class.java) ?: ""
                    val age = snapshot.child("age").getValue(Int::class.java) ?: 0
                    val email = snapshot.child("email").getValue(String::class.java) ?: ""
                    val address = snapshot.child("address").getValue(String::class.java) ?: ""

                    val patient = Patient(
                        id = patientId,
                        name = "$fName $lName",
                        phone = phone,
                        age = age,
                        email = email,
                        address = address
                    )
                    onSuccess(patient)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Failed to fetch patient")
            }
    }
}