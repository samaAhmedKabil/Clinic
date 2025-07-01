package com.example.clinic.ui.patient.commonQuestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.adapters.ExpandableQuestionsAdapter
import com.example.clinic.databinding.FragmentPatientCommonQuestionsBinding
import com.example.clinic.repos.CommonQuestionsRepo
import com.example.clinic.ui.doctor.commonQuestions.CommonQuestionsViewModel

class PatientCommonQuestionsFragment: Fragment() {
    private var _binding: FragmentPatientCommonQuestionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CommonQuestionsViewModel
    private lateinit var adapter: ExpandableQuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientCommonQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize ViewModel
        val repo = CommonQuestionsRepo()
        val factory = CommonQuestionsViewModel.CommonQuestionsViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[CommonQuestionsViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        arrowBackClick()
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = ExpandableQuestionsAdapter()
        binding.rvQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PatientCommonQuestionsFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            adapter.submitQuestions(questions) // Update RecyclerView with fetched questions
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}