package com.example.clinic.ui.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FeagmentMyProfileBinding
import com.example.clinic.repos.PatientRepo
import com.example.clinic.ui.doctor.viewModel.DoctorViewModel

class MyProfileFragment: Fragment() {
    private var _binding: FeagmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels { DoctorViewModel.Factory(PatientRepo()) }
    private lateinit var patientId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feagment_my_profile , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FeagmentMyProfileBinding.bind(view)
        backArrowClick()
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}