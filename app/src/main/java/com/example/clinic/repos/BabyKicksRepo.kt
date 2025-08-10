package com.example.clinic.repos

import com.example.clinic.data.BabyKicks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BabyKicksRepo {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("babyKicks")
    }

    private fun userId(): String? = auth.currentUser?.uid

    fun updateKicks(kicks: Int) {
        val uid = userId() ?: return
        val kicksData = BabyKicks(kicks, System.currentTimeMillis())
        dbRef.child(uid).setValue(kicksData)
    }

    fun resetKicks() {
        updateKicks(0)
    }

    fun getKicks(onDataChanged: (BabyKicks) -> Unit) {
        val uid = userId() ?: return
        dbRef.child(uid).get().addOnSuccessListener { snapshot ->
            snapshot.getValue(BabyKicks::class.java)?.let {
                onDataChanged(it)
            }
        }
        // Optional: Listen in real time
        dbRef.child(uid).addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                snapshot.getValue(BabyKicks::class.java)?.let {
                    onDataChanged(it)
                }
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })
    }
}