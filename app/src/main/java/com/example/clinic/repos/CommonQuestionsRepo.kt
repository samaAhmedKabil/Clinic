package com.example.clinic.repos

import android.util.Log
import com.example.clinic.data.CommonQuestions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class CommonQuestionsRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val commonQuestionsRef: DatabaseReference = database.getReference("common_questions")

    // Function to observe questions in real-time
    // Corrected Repository function
    fun getCommonQuestions(): Flow<List<CommonQuestions>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<CommonQuestions>()
                for (questionSnapshot in snapshot.children) {
                    // Firebase can now automatically map the 'id' field from the JSON
                    val question = questionSnapshot.getValue(CommonQuestions::class.java)

                    // Log to check if parsing is successful
                    Log.d("FirebaseRepo", "Parsed question: $question")
                    Log.d("FirebaseRepo", "Snapshot key: ${questionSnapshot.key}")

                    // Add the parsed object to the list if it's not null
                    question?.let { questions.add(it) }
                }
                // Log the final list size
                Log.d("FirebaseRepo", "Total questions received: ${questions.size}")
                trySend(questions).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error for debugging
                Log.e("FirebaseRepo", "Failed to load questions", error.toException())
                close(error.toException())
            }
        }

        commonQuestionsRef.addValueEventListener(valueEventListener)

        awaitClose { commonQuestionsRef.removeEventListener(valueEventListener) }
    }

    suspend fun uploadQuestion(question: CommonQuestions) {
        val newQuestionRef = commonQuestionsRef.push() // Generates a unique key
        val questionWithId = question.copy(id = newQuestionRef.key ?: "") // Set the ID
        newQuestionRef.setValue(questionWithId).await() // Upload data and wait for completion
    }
}