package com.example.clinic.ui.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.clinic.data.Feedback
import com.example.clinic.databinding.DialogRatingBinding
import com.example.clinic.repos.FeedbackRepo
import com.example.clinic.ui.settings.feedbacks.FeedbacksViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RatingDialog(): BottomSheetDialogFragment() {

    private var _binding: DialogRatingBinding? = null
    private val binding get() = _binding!!

    private var selectedStars = 0
    private lateinit var viewModel: FeedbacksViewModel
    private lateinit var userId: String
    private lateinit var userName: String
    private val itemId = "item123"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = FeedbackRepo()
        val factory = FeedbacksViewModel.FeedbackViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[FeedbacksViewModel::class.java]
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        setupStars()
        getUserInfoAndRole()

        binding.ok.setOnClickListener {
            if (selectedStars == 0) {
                Toast.makeText(requireContext(), "يجب اختيار نجمة واحدة علي الاقل", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val text = binding.editFeedback.text.toString()
            if (text.isNotBlank()) {
                val feedback = Feedback("", userId, userName, text, selectedStars)
                viewModel.uploadFeedback(itemId, feedback) {
                    if (it) binding.editFeedback.setText("")
                }
            }
            dismiss()
        }
    }

    private fun setupStars() {
        val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        stars.forEachIndexed { index, star ->
            star.setOnClickListener {
                selectedStars = index + 1
                stars.forEachIndexed { i, img ->
                    img.setColorFilter(if (i < selectedStars) Color.YELLOW else Color.GRAY)
                }
            }
        }
    }

    private fun getUserInfoAndRole() {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

        userRef.get().addOnSuccessListener { snapshot ->
            val fname = snapshot.child("fname").value?.toString() ?: ""
            val lname = snapshot.child("lname").value?.toString() ?: ""

            userName = "$fname $lname"
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "فشل تحميل معلومات المستخدم", Toast.LENGTH_SHORT).show()
        }
    }
}