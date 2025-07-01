package com.example.clinic.ui.doctor.cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clinic.repos.PatientRepo
import com.example.clinic.databinding.FragmentDoctorCaseDetailsBinding
import com.example.clinic.ui.doctor.viewModel.DoctorViewModel

class CaseDetailsFragment :Fragment() {
    private var _binding: FragmentDoctorCaseDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels { DoctorViewModel.Factory(PatientRepo()) }
    private lateinit var patientId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorCaseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backArrowClick()
        patientId = CaseDetailsFragmentArgs.fromBundle(requireArguments()).id
        binding.inProgress.visibility = View.VISIBLE
        viewModel.getPatientById(patientId)

        viewModel.patient.observe(viewLifecycleOwner) { patient ->
            if (patient != null) {
                binding.patientName.text = patient.name
                binding.email.setText(patient.email)
                binding.phone.setText(patient.phone)
                binding.age.setText(patient.age.toString())
                binding.address.setText(patient.address)
                binding.inProgress.visibility = View.GONE
            } else {
                binding.patientName.text = "No data found"
                binding.inProgress.visibility = View.GONE
            }
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