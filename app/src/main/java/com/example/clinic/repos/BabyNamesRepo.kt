package com.example.clinic.repos

import com.example.clinic.data.BabyName
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyNamesRepo {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("baby_names")

    fun addBabyName(name: String, gender: String) {
        val id = dbRef.push().key ?: return
        val babyName = BabyName(id, name, gender)
        dbRef.child(id).setValue(babyName)
    }

    fun getBabyNames(listener: (List<BabyName>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val names = mutableListOf<BabyName>()
                for (child in snapshot.children) {
                    child.getValue(BabyName::class.java)?.let { names.add(it) }
                }
                listener(names)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log or handle error
            }
        })
    }
}