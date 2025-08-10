package com.example.clinic.ui.patient.forYou.babyKicks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clinic.databinding.FragmentPatientBabyKicksBinding
import com.example.clinic.repos.BabyKicksRepo

class BabyKicksFragment: Fragment() {
    private var _binding: FragmentPatientBabyKicksBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BabyKicksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientBabyKicksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = BabyKicksRepo()
        val factory = BabyKicksViewModel.Factory(repo)
        viewModel = ViewModelProvider(this, factory)[BabyKicksViewModel::class.java]

        setupUI()
        observeKicks()
    }

    private fun setupUI() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.kicksCounter.setOnClickListener {
            viewModel.incrementKick()
        }

        binding.reset.setOnClickListener {
            viewModel.resetKicks()
        }
    }

    private fun observeKicks() {
        viewModel.kicks.observe(viewLifecycleOwner) { kicksData ->
            binding.kicksNum.text = kicksData.kicksCount.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}