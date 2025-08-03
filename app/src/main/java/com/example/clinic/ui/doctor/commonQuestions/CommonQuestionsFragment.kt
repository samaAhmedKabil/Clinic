package com.example.clinic.ui.doctor.commonQuestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.adapters.ExpandableQuestionsAdapter
import com.example.clinic.data.CommonQuestions
import com.example.clinic.databinding.FragmentDoctorAllCommonQuestionsBinding
import com.example.clinic.repos.CommonQuestionsRepo
import com.example.clinic.ui.dialogs.ShowEditDialog
import com.example.clinic.ui.patient.commonQuestions.PatientCommonQuestionsFragment

class CommonQuestionsFragment : Fragment() {

    private var _binding: FragmentDoctorAllCommonQuestionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CommonQuestionsViewModel
    private lateinit var adapter: ExpandableQuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorAllCommonQuestionsBinding.inflate(inflater, container, false)
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
        addClick()
        arrowBackClick()
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addClick(){
        binding.addNewQuestion.setOnClickListener {
            findNavController().navigate(CommonQuestionsFragmentDirections.actionCommonQuestionsFragmentToNewQuestionFragment())
        }
    }

    private fun showEditDialog(question: CommonQuestions) {
        ShowEditDialog(question) { newQ, newA ->
            viewModel.editQuestion(question.id, newQ, newA)
        }.show(parentFragmentManager, "EditQuestionBottomSheet")
    }

    private fun setupRecyclerView() {
        adapter = ExpandableQuestionsAdapter(
            onEditClick = { question -> showEditDialog(question) },
            onDeleteClick = { question -> viewModel.deleteQuestion(question.id) }
        )
        binding.rvQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CommonQuestionsFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            adapter.submitQuestions(questions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}