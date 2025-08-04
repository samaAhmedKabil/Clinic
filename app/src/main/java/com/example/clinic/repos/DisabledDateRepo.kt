package com.example.clinic.repos

import com.example.clinic.data.DisabledDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DisabledDatesRepo {
    private val db = FirebaseDatabase.getInstance().getReference("disabled_dates")

    fun addDisabledDate(date: DisabledDate, callback: (Boolean) -> Unit) {
        val key = db.push().key ?: return callback(false)
        db.child(key).setValue(date.copy(id = key))
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun observeDisabledDates(callback: (List<DisabledDate>) -> Unit) {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(DisabledDate::class.java) }
                callback(list)
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }

    fun deleteDisabledDate(date: DisabledDate) {
        if (date.id.isEmpty()) return
        db.child(date.id).removeValue()
    }
}