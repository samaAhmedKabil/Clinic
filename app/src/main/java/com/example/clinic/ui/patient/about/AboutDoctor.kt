package com.example.clinic.ui.patient.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentAboutDoctorBinding
import com.example.clinic.databinding.FragmentSettingsBinding


class AboutDoctor: Fragment() {
    private var _binding: FragmentAboutDoctorBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_doctor , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutDoctorBinding.bind(view)
        backArrowClick()
        bookClick()
    }

    private fun backArrowClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bookClick(){
        binding.book.setOnClickListener {
            findNavController().navigate(R.id.action_aboutDoctor_to_slotSelectionFragment)
        }
    }
}