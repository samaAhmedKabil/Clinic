package com.example.clinic.ui.doctor.cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic.adapters.AllCasesAdapter
import com.example.clinic.databinding.FragmentDoctorAllCasesBinding
import com.example.clinic.repos.PatientRepo
import com.example.clinic.ui.doctor.viewModel.DoctorViewModel

class AllCasesFragment :Fragment() {
    private var _binding: FragmentDoctorAllCasesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels { DoctorViewModel.Factory(PatientRepo()) }
    private lateinit var patientAdapter: AllCasesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorAllCasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backArrowClick()

        binding.inProgress.visibility = View.VISIBLE

        patientAdapter = AllCasesAdapter { patient ->
            // Handle patient click if needed
            Toast.makeText(requireContext(), "Selected: ${patient.name}", Toast.LENGTH_SHORT).show()
        }

        patientAdapter = AllCasesAdapter { selectedPatient ->
            val action = AllCasesFragmentDirections.actionAllCasesFragmentToCaseDetailsFragment(selectedPatient.id)
            findNavController().navigate(action)
        }

        binding.casesRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = patientAdapter
        }

        // Observe the patient list from the ViewModel
        viewModel.patientsList.observe(viewLifecycleOwner) { patients ->
            patientAdapter.submitList(patients)
            binding.inProgress.visibility = View.GONE
        }

        // Observe error messages if any
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            binding.inProgress.visibility = View.GONE
        }

        // Fetch patients from Firebase
        viewModel.fetchAllPatients()
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