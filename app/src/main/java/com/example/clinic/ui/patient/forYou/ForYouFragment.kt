package com.example.clinic.ui.patient.forYou

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.databinding.FragmentPatientForYouBinding

class ForYouFragment:Fragment() {

    private var _binding: FragmentPatientForYouBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrowBackClick()
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}