package com.example.clinic.repos

import com.example.clinic.data.Service
import com.example.clinic.utils.ConstData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ServicesRepo {

    private val database = FirebaseDatabase.getInstance().reference

    fun getUserRole(onResult: (Boolean) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        database.child("users").child(uid).child("role")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isDoctor = snapshot.getValue(String::class.java) == ConstData.DOCTOR_TYPE
                    onResult(isDoctor)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun loadServices(onResult: (List<Service>) -> Unit) {
        database.child("services").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val servicesList = mutableListOf<Service>()
                for (serviceSnap in snapshot.children) {
                    val service = serviceSnap.getValue(Service::class.java)
                    if (service != null) servicesList.add(service)
                }
                onResult(servicesList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun saveService(service: Service) {
        val key = service.id ?: database.child("services").push().key ?: return
        val updatedService = service.copy(id = key)
        database.child("services").child(key).setValue(updatedService)
    }
}