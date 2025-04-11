package com.example.clinic.ui.doctor.cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clinic.repos.PatientRepo
import com.example.clinic.databinding.FragmentCaseDetailsBinding
import com.example.clinic.ui.doctor.viewModel.DoctorViewModel

class CaseDetailsFragment :Fragment() {
    private var _binding: FragmentCaseDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels { DoctorViewModel.Factory(PatientRepo()) }
    private lateinit var patientId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCaseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backArrowClick()
        patientId = CaseDetailsFragmentArgs.fromBundle(requireArguments()).id

        viewModel.getPatientById(patientId)

        viewModel.patient.observe(viewLifecycleOwner) { patient ->
            if (patient != null) {
                binding.patientName.text = patient.name
                binding.phone.text = patient.phone
                binding.age.text = patient.age.toString()
                binding.email.text = patient.email
                binding.address.text = patient.address
            } else {
                binding.patientName.text = "No data found"
            }
        }
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}