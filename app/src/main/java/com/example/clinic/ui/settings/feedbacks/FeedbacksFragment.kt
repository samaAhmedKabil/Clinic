package com.example.clinic.ui.settings.feedbacks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.adapters.FeedbackAdapter
import com.example.clinic.databinding.FragmentBFeedbackBinding
import com.example.clinic.repos.FeedbackRepo
import com.example.clinic.ui.dialogs.RatingDialog
import com.example.clinic.utils.ConstData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FeedbacksFragment: Fragment() {
    private var _binding: FragmentBFeedbackBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FeedbacksViewModel
    private lateinit var userId: String
    private var isDoctor = false
    private val itemId = "item123"
    private lateinit var adapter: FeedbackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = FeedbackRepo()
        val factory = FeedbacksViewModel.FeedbackViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[FeedbacksViewModel::class.java]

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        viewModel.loadFeedbacks(itemId)

        observeFeedbacks()
        getUserInfoAndRole()
        backArrowClick()

        binding.feedbacksRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.addNewFeedback.setOnClickListener {
            val bottomSheetFragment = RatingDialog()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun observeFeedbacks() {
        viewModel.feedbacks.observe(viewLifecycleOwner) { list ->
            adapter = FeedbackAdapter(list, isDoctor) { feedback ->
                viewModel.deleteFeedback(itemId, feedback.id) { success ->
                    if (!success){
                        Toast.makeText(requireContext(), "فشل الحذف", Toast.LENGTH_SHORT).show()
                        binding.inProgress.visibility = View.GONE
                    }
                    else{
                        Toast.makeText(requireContext(), "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()
                        binding.inProgress.visibility = View.GONE
                    }
                }
            }
            binding.feedbacksRecycler.adapter = adapter
        }
    }

    private fun getUserInfoAndRole() {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

        userRef.get().addOnSuccessListener { snapshot ->
            val role = snapshot.child("role").value?.toString() ?: ""
            isDoctor = role == ConstData.DOCTOR_TYPE

            if (role == ConstData.DOCTOR_TYPE) {
                binding.addNewFeedback.visibility = View.VISIBLE
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "فشل تحميل معلومات المستخدم", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}