package com.example.clinic.ui.onBoarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clinic.R
import com.example.clinic.databinding.FragmentBOnboarding1Binding

class OnBoardingOne: Fragment() {
    private var _binding: FragmentBOnboarding1Binding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b_onboarding_1 , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBOnboarding1Binding.bind(view)
        onContinueClick()
    }

    private fun onContinueClick(){
        binding.getStarted.setOnClickListener {
            requireContext().getSharedPreferences("onboarding", Context.MODE_PRIVATE).edit() {putBoolean("shown", true)}
            findNavController().navigate(R.id.roleSelectionFragment)
        }
    }
}