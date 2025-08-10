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
        hospitalBagClick()
        babyNamesClick()
        babyKicksClick()
    }

    private fun arrowBackClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun hospitalBagClick(){
        binding.hospitalBag.setOnClickListener {
            findNavController().navigate(ForYouFragmentDirections.actionForYouFragmentToHospitalBagFragment())
        }
    }

    private fun babyNamesClick(){
        binding.babyNames.setOnClickListener {
            findNavController().navigate(ForYouFragmentDirections.actionForYouFragmentToBabyNamesFragment())
        }
    }

    private fun babyKicksClick(){
        binding.babyKicks.setOnClickListener {
            findNavController().navigate(ForYouFragmentDirections.actionForYouFragmentToBabyKicksFragment())
        }
    }
}