package com.example.clinic.ui.doctor.commonQuestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clinic.databinding.FragmentDoctorAllCommonQuestionsBinding
import com.example.clinic.databinding.FragmentDoctorNewCommonQuestionBinding
import com.example.clinic.repos.CommonQuestionsRepo

class NewQuestionFragment: Fragment() {

    private var _binding: FragmentDoctorNewCommonQuestionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CommonQuestionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorNewCommonQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val repo = CommonQuestionsRepo()
        val factory = CommonQuestionsViewModel.CommonQuestionsViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[CommonQuestionsViewModel::class.java]

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.post.setOnClickListener {
            val question = binding.editQuestion.text.toString().trim()
            val answer = binding.editAnswer.text.toString().trim()
            viewModel.uploadNewQuestion(question, answer)
        }
    }

    private fun setupObservers() {
        viewModel.uploadStatus.observe(viewLifecycleOwner) { statusMessage ->
            Toast.makeText(requireContext(), statusMessage, Toast.LENGTH_SHORT).show()
            if (statusMessage.contains("successfully")) {
                // Clear input fields on successful upload
                binding.editQuestion.text.clear()
                binding.editAnswer.text.clear()
                findNavController().popBackStack()
            }
        }
    }
}