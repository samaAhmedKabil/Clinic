package com.example.clinic.repos

import com.example.clinic.data.Feedback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedbackRepo {

    private val dbRef = FirebaseDatabase.getInstance().getReference("feedbacks")

    fun uploadFeedback(itemId: String, feedback: Feedback, onComplete: (Boolean) -> Unit) {
        val key = dbRef.child(itemId).push().key ?: return
        val newFeedback = feedback.copy(id = key)
        dbRef.child(itemId).child(key).setValue(newFeedback)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    fun getFeedbacks(itemId: String, onData: (List<Feedback>) -> Unit) {
        dbRef.child(itemId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Feedback>()
                snapshot.children.mapNotNullTo(list) {
                    it.getValue(Feedback::class.java)
                }
                onData(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun deleteFeedback(itemId: String, feedbackId: String, onComplete: (Boolean) -> Unit) {
        dbRef.child(itemId).child(feedbackId).removeValue()
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

}