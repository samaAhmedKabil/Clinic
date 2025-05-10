package com.example.clinic.repos

import com.example.clinic.data.DoctorBooking
import com.example.clinic.utils.ConstData
import com.example.clinic.data.Patient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientRepo {
    private val database = FirebaseDatabase.getInstance().getReference("users")
    private val bookingsDatabase = FirebaseDatabase.getInstance().getReference("bookings")

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

    fun getAllBookings(onResult: (List<DoctorBooking>) -> Unit, onFailure: (String) -> Unit) {
        bookingsDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookings = mutableListOf<DoctorBooking>()
                val bookingCount = snapshot.children.count()
                var processedCount = 0

                if (bookingCount == 0) {
                    onResult(emptyList())
                    return
                }

                for (bookingSnapshot in snapshot.children) {
                    val bookingId = bookingSnapshot.key ?: continue
                    val patientId = bookingSnapshot.child("patientId").getValue(String::class.java) ?: continue
                    val date = bookingSnapshot.child("date").getValue(String::class.java) ?: ""
                    val slot = bookingSnapshot.child("timeSlot").getValue(String::class.java) ?: ""

                    // Fetch patient details from users node
                    database.child(patientId).get()
                        .addOnSuccessListener { userSnapshot ->
                            val fName = userSnapshot.child("fname").getValue(String::class.java) ?: ""
                            val lName = userSnapshot.child("lname").getValue(String::class.java) ?: ""
                            val phone = userSnapshot.child("phone").getValue(String::class.java) ?: ""

                            val booking = DoctorBooking(
                                bookingId = bookingId,
                                patientName = "$fName $lName",
                                patientPhone = phone,
                                date = date,
                                slot = slot
                            )
                            bookings.add(booking)

                            processedCount++
                            if (processedCount == bookingCount) {
                                onResult(bookings)
                            }
                        }
                        .addOnFailureListener { exception ->
                            processedCount++
                            if (processedCount == bookingCount) {
                                onResult(bookings)
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }
        })
    }
}